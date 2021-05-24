package ru.klyuv.barcode.presentation

import ru.klyuv.core.common.ui.BaseViewModel
import ru.klyuv.core.model.BarcodeModel
import ru.klyuv.core.usecase.BarcodeScreenUseCase
import javax.inject.Inject

class BarcodeListViewModel
@Inject constructor(
    private val barcodeScreenUseCase: BarcodeScreenUseCase
) : BaseViewModel() {

    val barcodeLiveData = barcodeScreenUseCase.barcodeListLiveData

    fun addBarcode(barcode: BarcodeModel) {
        barcodeScreenUseCase.addBarcode(barcode)
    }

    fun addBarcodes(barcode: List<BarcodeModel>) {
        barcodeScreenUseCase.addBarcodes(barcode)
    }

    fun deleteBarcode(barcode: Int) {
        barcodeScreenUseCase.deleteBarcode(barcode)
    }

}