package ru.klyuv.barcode.presentation.camera

import android.graphics.Rect
import android.media.Image
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.mlkit.common.MlKitException
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import ru.klyuv.barcode.common.convertYuv420888ImageToBitmap
import ru.klyuv.barcode.common.rotateAndCrop
import ru.klyuv.core.common.extensions.sendErrorLog
import ru.klyuv.core.common.extensions.sendLog
import ru.klyuv.core.model.state.CameraHolderState

/**
 * Данная реализация [ImageAnalysis.Analyzer] обрезает входящее изображение
 * и ищет на изображении Barcode [com.google.mlkit.vision.barcode.Barcode]
 *
 * @param lifecycle
 * @param imageCropPercentages - служит для задания процента обрезки входящего изображения
 * @param barCodeDetected - лямбда для разобранного Barcode [com.google.mlkit.vision.barcode.Barcode]
 *
 * Принцип работы:
 * 1) Принимает изображение
 * 2) Конвертируем входящее изображение в Bitmap
 * 3) Проверяем положение
 * 4) Обрезаем полученный Bitmap и переворачиваем его
 * 5) Преобразуем в [com.google.mlkit.vision.common.InputImage]
 * 6) Преобразованное конечное изображение передаём на анализ
 *
 * Обязательно при завершении работы с ImageProxy его необходимо закрыть.
 * Входящее изображение имеет дефолтное разрешение 1280x720, хотя в документации
 * сказано что оно 640x480
 * [androidx.camera.core] предоставляет изображение в формате YUV_420_888
 */
class BarCodeImageAnalyzer(
    lifecycle: Lifecycle,
    private val imageCropPercentages: MutableLiveData<CameraHolderState>,
    private val barCodeDetected: (barCode: String) -> Unit
) : ImageAnalysis.Analyzer {

    private val detector = BarcodeScanning.getClient()

    init {
        lifecycle.addObserver(detector)
    }

    @androidx.camera.core.ExperimentalGetImage
    override fun analyze(image: ImageProxy) {
        val mediaImage = image.image ?: return

        when (imageCropPercentages.value) {
            is CameraHolderState.HolderDraw -> checkHolderDrawState(image, mediaImage)
            is CameraHolderState.Failure -> checkHolderFailureState(image, mediaImage)
            is CameraHolderState.WithoutHolder -> checkHolderFailureState(image, mediaImage)
            null -> {
                image.close()
                return
            }
        }
    }

    private fun checkHolderDrawState(image: ImageProxy, mediaImage: Image) {
        val rotationDegrees = image.imageInfo.rotationDegrees

        /**
         *  Мы запрашиваем setTargetAspectRatio, но не гарантируем что камера на устройстве
         *  поддерживает это. Поэтому мы вычисляем фактическое соотношение по первому кадру
         *  чтобы знать как правильно обрезать изображение для сканирования.
         */
        val imageHeight = mediaImage.height
        val imageWidth = mediaImage.width

        val actualAspectRatio = imageWidth / imageHeight

        val convertImageToBitmap = mediaImage.convertYuv420888ImageToBitmap()

        val cropRect = Rect(0, 0, imageWidth, imageHeight)

        val liveDataState = imageCropPercentages.value as? CameraHolderState.HolderDraw ?: return
        if (actualAspectRatio > 3) {
            val originalHeightCropPercentage = liveDataState.first
            val originalWidthCropPercentage = liveDataState.second
            imageCropPercentages.postValue(
                CameraHolderState.HolderDraw(
                    originalHeightCropPercentage / 2,
                    originalWidthCropPercentage
                )
            )
        }

        /**
         * Если изображение повернуто на 90 или 270 градусов, то при обрезке надо
         * поменять местами высоту и ширину
         */
        val cropPercentages = imageCropPercentages.value as? CameraHolderState.HolderDraw ?: return
        val heightCropPercent = cropPercentages.first
        val widthCropPercent = cropPercentages.second
        val (widthCrop, heightCrop) = when (rotationDegrees) {
            90, 270 -> Pair(heightCropPercent / 100f, widthCropPercent / 100f)
            else -> Pair(widthCropPercent / 100f, heightCropPercent / 100f)
        }

        cropRect.inset(
            (imageWidth * widthCrop / 2).toInt(),
            (imageHeight * heightCrop / 2).toInt()
        )

        if (cropRect.height() > 0 && cropRect.width() > 0) {
            val croppedBitmap = rotateAndCrop(convertImageToBitmap, rotationDegrees, cropRect)
            recognizeBarCode(InputImage.fromBitmap(croppedBitmap, 0)).addOnSuccessListener {
                image.close()
            }
        } else {
            imageCropPercentages.postValue(CameraHolderState.Failure)
            recognizeBarCode(InputImage.fromBitmap(convertImageToBitmap, 0)).addOnSuccessListener {
                image.close()
            }
        }
    }

    private fun checkHolderFailureState(image: ImageProxy, mediaImage: Image) {
        recognizeBarCode(
            InputImage.fromMediaImage(
                mediaImage,
                image.imageInfo.rotationDegrees
            )
        ).addOnSuccessListener {
            image.close()
        }
    }

    private fun recognizeBarCode(
        image: InputImage
    ): Task<List<Barcode>> =
        detector.process(image)
            .addOnSuccessListener {
                if (it.isNotEmpty()) checkList(it)
            }
            .addOnFailureListener {
                sendErrorLog(TAG, "Barcode recognition error", it)
                sendLog(TAG, "Barcode recognition error: ${getErrorMessage(it)}")
            }

    private fun checkList(list: List<Barcode>) {
        run loop@{
            list.forEach { barcode ->
                if (!barcode.displayValue.isNullOrEmpty()) sendBarCode(barcode)
            }
        }
    }

    private fun sendBarCode(barCode: Barcode) {
        barCodeDetected(barCode.displayValue!!)
    }

    private fun getErrorMessage(exception: Exception): String? {
        val mlKitException = exception as? MlKitException ?: return exception.message
        return if (mlKitException.errorCode == MlKitException.UNAVAILABLE) {
            "UNAVAILABLE Error"
        } else exception.message
    }

    companion object {
        private const val TAG = "BarCodeAnalyzer"
    }
}