package ru.klyuv.data.request.network

import retrofit2.Response
import ru.klyuv.core.common.data.NetworkFailure
import ru.klyuv.core.common.data.Result

interface NetworkRequest {

    suspend fun <T, R> make(
        request: suspend () -> Response<T>,
        transform: suspend (T?) -> R
    ): Result<NetworkFailure, R>

}