package ru.klyuv.core.usecase

import androidx.lifecycle.LiveData
import ru.klyuv.core.model.BarcodeModel

interface BarcodeScreenUseCase {

    val barcodeListLiveData: LiveData<List<BarcodeModel>>

    fun addBarcode(barcode: BarcodeModel)

    fun deleteBarcode(barcode: BarcodeModel)

}