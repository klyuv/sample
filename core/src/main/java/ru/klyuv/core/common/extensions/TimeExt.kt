package ru.klyuv.core.common.extensions

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun String.formatDate(): String {
    return if (this.isEmpty()) ""
    else {
        return try {
            val spf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val spfBeauty = SimpleDateFormat("dd.MM.yyyy HH:mm")
            spf.timeZone = TimeZone.getTimeZone("GMT")
            spfBeauty.format(spf.parse(this))
        } catch (e: ParseException) {
            ""
        }
    }
}