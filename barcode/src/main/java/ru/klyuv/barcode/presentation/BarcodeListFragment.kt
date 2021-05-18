package ru.klyuv.barcode.presentation

import android.Manifest
import android.content.Context
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.klyuv.barcode.R
import ru.klyuv.barcode.databinding.FragmentBarcodeListBinding
import ru.klyuv.barcode.presentation.camera.CameraFragment
import ru.klyuv.core.common.extensions.*
import ru.klyuv.core.common.ui.BaseFragment

class BarcodeListFragment : BaseFragment() {

    private val viewModel: BarcodeListViewModel by androidLazy { getViewModel(viewModelFactory) }
    private val viewBinding by viewBinding(FragmentBarcodeListBinding::bind)

    lateinit var requestPermission: ActivityResultLauncher<String>

    override fun getLayoutID(): Int = R.layout.fragment_barcode_list

    override fun setUI(savedInstanceState: Bundle?) {
        with(viewBinding) {
            btnCamera.setOnClickListener { requestPermission.launch(Manifest.permission.CAMERA) }
        }
        setCameraListener()
    }

    override fun observeViewModel() {

    }

    private fun setCameraListener() {
        setFragmentResultListener(CameraFragment.CAMERA_RESULT_REQUEST_KEY) { key, bundle ->
            val data = bundle.getString(CameraFragment.BARCODE_KEY)
//            viewModel.addBarcode()
            sendLog("$data")
            bundle.clear()
        }
    }

    private fun openCamera() {
        findNavController().navigate(
            BarcodeListFragmentDirections.actionBarcodeListFragmentToCameraFragment()
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requestPermission =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
                if (it) openCamera()
                else showErrorPermissionMessage(
                    PermissionsError.CAMERA
                )
            }
    }
}