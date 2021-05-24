package ru.klyuv.barcode.common

import com.google.mlkit.vision.barcode.Barcode
import ru.klyuv.core.model.BarcodeModel
import ru.klyuv.core.model.BarcodeType


fun Barcode.toBarcodeModel(): BarcodeModel =
    BarcodeModel(
        type = this.valueType.toBarcodeType(),
        value = this.displayValue
    )

private fun Int.toBarcodeType(): BarcodeType =
    when(this) {
        1 -> BarcodeType.CONTACT_INFO
        2 -> BarcodeType.EMAIL
        3 -> BarcodeType.ISBN
        4 -> BarcodeType.PHONE
        5 -> BarcodeType.PRODUCT
        6 -> BarcodeType.SMS
        7 -> BarcodeType.TEXT
        8 -> BarcodeType.URL
        9 -> BarcodeType.WIFI
        10 -> BarcodeType.GEO
        11 -> BarcodeType.CALENDAR_EVENT
        12 -> BarcodeType.DRIVER_LICENSE
        else -> BarcodeType.UNKNOWN
    }