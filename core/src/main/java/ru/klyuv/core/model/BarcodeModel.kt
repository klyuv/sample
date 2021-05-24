package ru.klyuv.core.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class BarcodeModel(
    val type: BarcodeType,
    val value: String?
) : Parcelable

enum class BarcodeType {
    CONTACT_INFO,
    EMAIL,
    ISBN,
    PHONE,
    PRODUCT,
    SMS,
    TEXT,
    URL,
    WIFI,
    GEO,
    CALENDAR_EVENT,
    DRIVER_LICENSE,
    UNKNOWN
}