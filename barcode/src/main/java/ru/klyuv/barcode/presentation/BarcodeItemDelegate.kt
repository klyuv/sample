package ru.klyuv.barcode.presentation

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import ru.klyuv.barcode.R
import ru.klyuv.barcode.databinding.RowBarcodeBinding
import ru.klyuv.core.model.BarcodeModel
import ru.klyuv.core.model.BarcodeType

object BarcodeItemDelegate {

    fun init() = adapterDelegateViewBinding<BarcodeModel, BarcodeModel, RowBarcodeBinding>(
        { layoutInflater, parent ->
            RowBarcodeBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        }
    ) {
        bind {
            with(binding) {
                tvBarcodeValue.text = item.value ?: getString(R.string.no_data)
                ivBarcodeType.setImageResource(
                    when (item.type) {
                        BarcodeType.TEXT -> R.drawable.ic_vector_text
                        BarcodeType.WIFI -> R.drawable.ic_vector_wifi
                        BarcodeType.PHONE -> R.drawable.ic_vector_phone
                        BarcodeType.PRODUCT -> R.drawable.ic_vector_barcode
                        else -> R.drawable.ic_vector_question
                    }
                )
            }
        }
    }

}