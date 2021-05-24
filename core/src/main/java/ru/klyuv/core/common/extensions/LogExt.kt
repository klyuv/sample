package ru.klyuv.core.common.extensions

import android.util.Log
import ru.klyuv.core.BuildConfig

private const val DEFAULT_LOG = "KlyuvSample"

fun sendLog(message: String) {
    if (BuildConfig.DEBUG && message.isNotEmpty()) {
        Log.d(DEFAULT_LOG, message)
    }
}

fun sendLog(TAG: String = DEFAULT_LOG, message: String) {
    if (BuildConfig.DEBUG && message.isNotEmpty()) {
        Log.d(TAG, message)
    }
}

fun sendLog(t: Throwable) {
    if (BuildConfig.DEBUG) {
        t.printStackTrace()
    }
}

fun sendErrorLog(tag: String = DEFAULT_LOG, message: String, t: Throwable) {
    if (BuildConfig.DEBUG && message.isNotEmpty()) {
        Log.e(tag, message, t)
    }
}