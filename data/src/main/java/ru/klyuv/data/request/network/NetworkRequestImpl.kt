package ru.klyuv.data.request.network

import retrofit2.Response
import ru.klyuv.core.common.data.NetworkFailure
import ru.klyuv.core.common.data.Result
import ru.klyuv.data.common.isSucceed
import ru.klyuv.data.common.mapToFailureState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkRequestImpl
@Inject constructor() : NetworkRequest {

    override suspend fun <T, R> make(
        request: suspend () -> Response<T>,
        transform: suspend (T?) -> R
    ): Result<NetworkFailure, R> =
        try {
            val response = request()
            when {
                response.isSucceed() -> Result.Success(transform(response.body()!!))
                response.code() == 204 -> Result.Success(transform(null))
                else -> {
                    Result.Error(
                        response.code().mapToFailureState()
                    )
                }
            }
        } catch (t: Throwable) {
            Result.Error(t.mapToFailureState())
        }

}