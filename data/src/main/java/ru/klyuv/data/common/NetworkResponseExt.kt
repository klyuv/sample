package ru.klyuv.data.common

import retrofit2.Response
import ru.klyuv.core.common.data.NetworkFailure
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.CancellationException

fun <T> Response<T>.isSucceed(): Boolean {
    return isSuccessful && body() != null
}

fun Int.mapToFailureState(): NetworkFailure =
    when (this) {
        401 -> NetworkFailure.HTTP401
        422 -> NetworkFailure.HTTP422
        400 -> NetworkFailure.HTTP400
        403 -> NetworkFailure.HTTP403
        500 -> NetworkFailure.HTTP500
        else -> NetworkFailure.HTTPUnknown(Throwable())
    }

fun Throwable.mapToFailureState(): NetworkFailure =
    when (this) {
        is UnknownHostException -> NetworkFailure.NetworkConnection
        is CancellationException -> NetworkFailure.Ignore
        is SocketTimeoutException -> NetworkFailure.TIMEOUT
        else -> NetworkFailure.Unknown(this)
    }