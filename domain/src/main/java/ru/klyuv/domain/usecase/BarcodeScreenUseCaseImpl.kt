package ru.klyuv.domain.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.klyuv.core.model.BarcodeModel
import ru.klyuv.core.usecase.BarcodeScreenUseCase
import javax.inject.Inject

class BarcodeScreenUseCaseImpl
    @Inject constructor(): BarcodeScreenUseCase {

    private val barcodeArrayList: MutableList<BarcodeModel> = mutableListOf()

    private val barcodeMutableLiveData = MutableLiveData<List<BarcodeModel>>()

    override val barcodeListLiveData: LiveData<List<BarcodeModel>>
        get() = barcodeMutableLiveData


    override fun addBarcode(barcode: BarcodeModel) {
        barcodeArrayList.add(barcode)
        postBarcodeData()
    }

    override fun deleteBarcode(barcode: BarcodeModel) {
        barcodeArrayList.remove(barcode)
        postBarcodeData()
    }

    private fun postBarcodeData() {
        barcodeMutableLiveData.value = barcodeArrayList
    }
}