package ru.klyuv.barcode.presentation

import android.Manifest
import android.content.Context
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import ru.klyuv.barcode.R
import ru.klyuv.barcode.databinding.FragmentBarcodeListBinding
import ru.klyuv.barcode.presentation.camera.CameraFragment
import ru.klyuv.core.common.extensions.*
import ru.klyuv.core.common.ui.BaseFragment
import ru.klyuv.core.model.BarcodeModel

class BarcodeListFragment : BaseFragment() {

    private val viewModel: BarcodeListViewModel by androidLazy { getViewModel(viewModelFactory) }
    private val viewBinding by viewBinding(FragmentBarcodeListBinding::bind)

    private lateinit var requestPermission: ActivityResultLauncher<String>

    override fun getLayoutID(): Int = R.layout.fragment_barcode_list

    private val barcodeAdapter by androidLazy {
        ListDelegationAdapter<List<BarcodeModel>>(
            BarcodeItemDelegate.init()
        )
    }

    override fun setUI(savedInstanceState: Bundle?) {
        with(viewBinding) {
            btnCamera.setOnClickListener { requestPermission.launch(Manifest.permission.CAMERA) }
            rvBarcode.apply {
                adapter = barcodeAdapter
                layoutManager = LinearLayoutManager(this.context)
                setHasFixedSize(true)
            }
            val swipeHandler = object: SwipeToDeleteCallback(
                requireContext(),
                R.drawable.ic_vector_delete
            ) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    viewModel.deleteBarcode(viewHolder.absoluteAdapterPosition)
                }
            }
            val itemTouchHelper = ItemTouchHelper(swipeHandler)
            itemTouchHelper.attachToRecyclerView(viewBinding.rvBarcode)
        }
        setCameraListener()
    }

    override fun observeViewModel() {
        observe(viewModel.barcodeLiveData, ::setBarcodeData)
    }

    private fun setBarcodeData(data: List<BarcodeModel>) {
        barcodeAdapter.items = data
        barcodeAdapter.notifyDataSetChanged()
    }

    private fun setCameraListener() {
        setFragmentResultListener(CameraFragment.CAMERA_RESULT_REQUEST_KEY) { _, bundle ->
            val data = bundle.getParcelable<BarcodeModel>(CameraFragment.BARCODE_KEY)
            if (data != null) viewModel.addBarcode(data)
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