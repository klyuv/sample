package ru.klyuv.barcode.presentation.camera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.*
import android.hardware.display.DisplayManager
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.SurfaceHolder
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.core.Camera
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.mlkit.vision.barcode.Barcode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import ru.klyuv.barcode.R
import ru.klyuv.barcode.common.addSurfaceCreatedListener
import ru.klyuv.barcode.common.aspectRatio
import ru.klyuv.barcode.databinding.FragmentCameraBinding
import ru.klyuv.core.common.extensions.*
import ru.klyuv.core.common.ui.BaseDialogFullScreenFragment
import ru.klyuv.core.model.state.CameraHolderState
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit

class CameraFragment : BaseDialogFullScreenFragment() {

    private val viewModel: CameraViewModel by androidLazy { getViewModel(viewModelFactory) }
    private val displayManager by lazy {
        requireContext().getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
    }

    private var displayId = -1
    private var preview: Preview? = null

    private var imageAnalyzer: ImageAnalysis? = null
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null

    private var useAllFrame: Boolean = false

    lateinit var requestPermission: ActivityResultLauncher<String>

    private val displayListener = object : DisplayManager.DisplayListener {
        override fun onDisplayAdded(p0: Int) = Unit
        override fun onDisplayRemoved(p0: Int) = Unit

        override fun onDisplayChanged(p0: Int) = view?.let { view ->
            if (displayId == this@CameraFragment.displayId) imageAnalyzer?.targetRotation =
                view.display.rotation
        } ?: Unit
    }

    private val viewBinding: FragmentCameraBinding by viewBinding(FragmentCameraBinding::bind)

    override fun getLayoutID(): Int = R.layout.fragment_camera

    override fun setUI(savedInstanceState: Bundle?) {
        with(viewBinding) {
            overlay.apply {
                toVisible()
                setZOrderOnTop(true)
                holder.setFormat(PixelFormat.TRANSPARENT)
                holder.addSurfaceCreatedListener { holder ->
                    drawOverlay(
                        holder,
                        DESIRED_HEIGHT_CROP_PERCENT,
                        DESIRED_WIDTH_CROP_PERCENT
                    )
                }
            }
        }

        requestPermission.launch(Manifest.permission.CAMERA)
    }

    private fun createCamera() {
        displayManager.registerDisplayListener(displayListener, null)
        with(viewBinding) {
            viewFinder.post {
                displayId = viewFinder.display.displayId
                bindCameraUseCase()
            }
        }
    }

    private fun bindCameraUseCase() {
        if (cameraProvider != null) bindCameraPreview()
        else {
            val processCameraProvider = ProcessCameraProvider.getInstance(requireContext())
            processCameraProvider.addListener({
                try {
                    cameraProvider = processCameraProvider.get()
                } catch (e: ExecutionException) {
                    throw IllegalStateException("Camera initialization failed", e.cause!!)
                }
                bindCameraPreview()
            }, Dispatchers.Main.asExecutor())
        }

    }

    private fun bindCameraPreview() {
        val metrics = DisplayMetrics().also { viewBinding.viewFinder.display.getRealMetrics(it) }
        val screenAspectRatio = aspectRatio(metrics.widthPixels, metrics.heightPixels)
        val rotation = viewBinding.viewFinder.display.rotation

        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        preview = Preview.Builder()
            .setTargetAspectRatio(screenAspectRatio)
            .setTargetRotation(rotation)
            .build()

        imageAnalyzer = ImageAnalysis.Builder()
            .setTargetAspectRatio(screenAspectRatio)
            .setTargetRotation(rotation)
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also {
                it.setAnalyzer(
                    Dispatchers.Default.asExecutor(),
                    BarCodeImageAnalyzer(
                        lifecycle,
                        viewModel.cropPercentLiveData,
                        ::sendCode
                    )
                )
            }

        cameraProvider?.unbindAll()

        try {
            camera = cameraProvider?.bindToLifecycle(
                this, cameraSelector, preview, imageAnalyzer
            )
            preview?.setSurfaceProvider(viewBinding.viewFinder.surfaceProvider)
        } catch (e: Exception) {
            sendErrorLog(message = "Use case binding failed", t = e)
        }

        viewBinding.viewFinder.afterMeasured {
            val factory: MeteringPointFactory = SurfaceOrientedMeteringPointFactory(
                viewBinding.viewFinder.width.toFloat(), viewBinding.viewFinder.height.toFloat()
            )
            val centerWidth = viewBinding.viewFinder.width.toFloat() / 2
            val centerHeight = viewBinding.viewFinder.height.toFloat() / 2

            val autoFocusPoint = factory.createPoint(centerWidth, centerHeight)

            try {
                camera?.cameraControl?.startFocusAndMetering(
                    FocusMeteringAction.Builder(
                        autoFocusPoint,
                        FocusMeteringAction.FLAG_AF
                    )
                        .setAutoCancelDuration(1, TimeUnit.SECONDS)
                        .build()
                )
            } catch (e: CameraInfoUnavailableException) {
                sendErrorLog(message = "Cannot access camera", t = e)
            }
        }
    }

    override fun observeViewModel() {
        observe(viewModel.cropPercentLiveData, ::checkCameraState)
        observe(viewBinding.viewFinder.previewStreamState, ::checkPreviewStreamState)
    }

    private fun checkPreviewStreamState(state: PreviewView.StreamState) {
        if (state == PreviewView.StreamState.STREAMING) {
            viewBinding.btnFlash.toVisibleOrGone(isFlashAvailable())

            viewModel.cropPercentLiveData.value = CameraHolderState.HolderDraw(
                DESIRED_HEIGHT_CROP_PERCENT,
                DESIRED_WIDTH_CROP_PERCENT
            )


            viewBinding.btnFlash.setOnClickListener { checkTorch() }

            viewBinding.btnHolder.setOnClickListener { checkAndChangeCameraFrame() }
        }
    }

    private fun checkCameraState(state: CameraHolderState) {
        when (state) {
            is CameraHolderState.HolderDraw -> setCameraHolderDrawState(state.first, state.second)
            is CameraHolderState.Failure -> setCameraHolderFailureState()
            is CameraHolderState.WithoutHolder -> setCameraWithoutHolder()
        }
    }

    private fun setCameraHolderDrawState(first: Int, second: Int) {
        viewBinding.btnHolder.toVisible()
        drawOverlay(
            viewBinding.overlay.holder,
            first,
            second
        )
    }

    private fun setCameraHolderFailureState() {
        viewBinding.btnHolder.toGone()
        useAllFrame = true
        drawOverlay(
            viewBinding.overlay.holder,
            0,
            0
        )
    }

    private fun setCameraWithoutHolder() {
        viewBinding.overlay.toGone()
    }

    private fun sendCode(barcode: String) {
        if (this@CameraFragment.isVisible) {
            imageAnalyzer?.clearAnalyzer()
            setFragmentResult(
                CAMERA_RESULT_REQUEST_KEY,
                bundleOf(BARCODE_KEY to barcode)
            )
            dismiss()
        }
    }

    private fun drawOverlay(
        holder: SurfaceHolder,
        heightCropPercent: Int,
        widthCropPercent: Int
    ) {
        holder.lockCanvas()?.let {
            it.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
            holder.unlockCanvasAndPost(it)
        }
        holder.lockCanvas()?.let { canvas ->
            val bgPaint = Paint().apply { alpha = 140 }
            canvas.drawPaint(bgPaint)
            val rectPaint = Paint().apply {
                xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
                style = Paint.Style.FILL
                color = Color.WHITE
            }
            val outlinePaint = Paint().apply {
                style = Paint.Style.STROKE
                color = Color.WHITE
                strokeWidth = 4f
            }

            val surfaceWidth = holder.surfaceFrame.width()
            val surfaceHeight = holder.surfaceFrame.height()

            val cornerRadius = 25f

            val rectTop = surfaceHeight * heightCropPercent / 2 / 100f
            val rectLeft = surfaceWidth * widthCropPercent / 2 / 100f
            val rectRight = surfaceWidth * (1 - widthCropPercent / 2 / 100f)
            val rectBottom = surfaceHeight * (1 - heightCropPercent / 2 / 100f)
            val rect = RectF(rectLeft, rectTop, rectRight, rectBottom)

            if (!useAllFrame) {
                val btnFrameTop = viewBinding.btnHolder.top.toFloat()
                val btnFrameBottom = viewBinding.btnHolder.bottom.toFloat()
                val btnFrameLeft = viewBinding.btnHolder.left.toFloat()
                val btnFrameRight = viewBinding.btnHolder.right.toFloat()
                val frameRect = RectF(btnFrameLeft, btnFrameTop, btnFrameRight, btnFrameBottom)

                canvas.drawRoundRect(frameRect, 100f, 100f, rectPaint)

                if (isFlashAvailable()) {
                    val btnFlashTop = viewBinding.btnFlash.top.toFloat()
                    val btnFlashBottom = viewBinding.btnFlash.bottom.toFloat()
                    val btnFlashLeft = viewBinding.btnFlash.left.toFloat()
                    val btnFlashRight = viewBinding.btnFlash.right.toFloat()
                    val flashRect = RectF(btnFlashLeft, btnFlashTop, btnFlashRight, btnFlashBottom)

                    canvas.drawRoundRect(flashRect, 100f, 100f, rectPaint)

                }

            }

            canvas.drawRoundRect(rect, cornerRadius, cornerRadius, rectPaint)
            canvas.drawRoundRect(rect, cornerRadius, cornerRadius, outlinePaint)
            holder.unlockCanvasAndPost(canvas)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) createCamera()
            else showErrorPermissionMessage(
                PermissionsError.CAMERA
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        camera?.let {
            it.cameraInfo.torchState.value == TorchState.OFF
        }
        imageAnalyzer?.clearAnalyzer()
        cameraProvider?.unbindAll()
        displayManager.unregisterDisplayListener(displayListener)
    }

    override fun onResume() {
        super.onResume()
        camera?.let {
            if (it.cameraInfo.torchState.value == TorchState.OFF) viewBinding.btnFlash.setImageDrawable(
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_vector_flashlight_on)
            )
            else viewBinding.btnFlash.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_vector_flashlight_off
                )
            )
        }
    }

    private fun checkTorch() {
        camera?.let {
            if (it.cameraInfo.torchState.value == TorchState.ON) {
                it.cameraControl.enableTorch(false)
                viewBinding.btnFlash.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_vector_flashlight_on
                    )
                )
            } else {
                it.cameraControl.enableTorch(true)
                viewBinding.btnFlash.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_vector_flashlight_off
                    )
                )
            }
        }
    }

    private fun checkAndChangeCameraFrame() {
        viewBinding.let {
            if (!useAllFrame) {
                useAllFrame = true
                it.btnHolder.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_vector_fullscreen_exit
                    )
                )
                viewModel.cropPercentLiveData.value = CameraHolderState.HolderDraw(
                    0,
                    0
                )
            } else {
                useAllFrame = false
                it.btnHolder.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_vector_fullscreen
                    )
                )
                viewModel.cropPercentLiveData.value = CameraHolderState.HolderDraw(
                    DESIRED_HEIGHT_CROP_PERCENT,
                    DESIRED_WIDTH_CROP_PERCENT
                )
            }
        }
    }

    private fun isFlashAvailable() = requireContext().packageManager
        .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)

    companion object {
        const val DESIRED_WIDTH_CROP_PERCENT = 20
        const val DESIRED_HEIGHT_CROP_PERCENT = 74
        const val CAMERA_RESULT_REQUEST_KEY = "1255"
        const val BARCODE_KEY = "barcode"
    }

}