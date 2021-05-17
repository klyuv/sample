package ru.klyuv.core.data

import androidx.annotation.StringRes

sealed class Result<out L, out R> {

    data class Error<out L>(val a: L) : Result<L, Nothing>()

    data class Success<out R>(val b: R) : Result<Nothing, R>()

    val isSuccess get() = this is Success<R>
    val isError get() = this is Error<L>

    fun <L> left(a: L) = Error(a)
    fun <R> right(b: R) = Success(b)

    fun <T> either(functionLeft: (L) -> T, functionRight: (R) -> T): T =
        when (this) {
            is Error -> functionLeft(a)
            is Success -> functionRight(b)
        }

    suspend fun <T> eitherSuspend(
        functionLeft: suspend (L) -> T,
        functionRight: suspend (R) -> T
    ): T =
        when (this) {
            is Error -> functionLeft(a)
            is Success -> functionRight(b)
        }

    suspend fun eitherSuspendRight(
        functionLeft: (L) -> Unit,
        functionRight: suspend (R) -> Unit
    ): Unit =
        when (this) {
            is Error -> functionLeft(a)
            is Success -> functionRight(b)
        }

    suspend fun eitherSuspendBoth(
        functionLeft: suspend (L) -> Unit,
        functionRight: suspend (R) -> Unit
    ): Unit =
        when (this) {
            is Error -> functionLeft(a)
            is Success -> functionRight(b)
        }
}

fun <A, B, C> ((A) -> B).compose(f: (B) -> C): (A) -> C = {
    f(this(it))
}

fun <T, L, R> Result<L, R>.flatMap(fn: (R) -> Result<L, T>): Result<L, T> {
    return when (this) {
        is Result.Error -> Result.Error(a)
        is Result.Success -> fn(b)
    }
}

suspend fun <T, L, R> Result<L, R>.suspendFlatMap(fn: suspend (R) -> Result<L, T>): Result<L, T> {
    return when (this) {
        is Result.Error -> Result.Error(a)
        is Result.Success -> fn(b)
    }
}

fun <T, L, R> Result<L, R>.map(fn: (R) -> (T)): Result<L, T> {
    return this.flatMap(fn.compose(::right))
}

fun <L, R> Result<L, R>.onNext(fn: (R) -> Unit): Result<L, R> {
    this.flatMap(fn.compose(::right))
    return this
}

fun <L, R> Result<L, R>.nullIfError(): R? =
    when (this) {
        is Result.Success -> this.b
        else -> null
    }

inline fun <L, R> Result<L, R>.nullIfError(onError: (L) -> Unit): R? =
    when (this) {
        is Result.Success -> this.b
        is Result.Error -> {
            onError(this.a)
            null
        }
    }

sealed class Failure {

    class SimpleFailure(val error: String) : Failure()

    class SimpleFailureRes(@StringRes val error: Int) : Failure()

    object ServerFailure : Failure()

    object NetworkConnection : Failure()

    class HTTP401(val error: String) : Failure()

    object HTTP403 : Failure()

    object HTTP204 : Failure()

    object HTTP500 : Failure()

    object TIMEOUT : Failure()

    class HTTPUnknown(val error: Throwable) : Failure()

    class Unknown(val error: Throwable) : Failure()

    object Ignore : Failure()

    object HTTP422 : Failure()

    object HTTP400 : Failure()

    class SimpleFailureResWithDismissDialog(@StringRes val error: Int) : Failure()

    class SimpleFailureResWithShowAlertDialog(@StringRes val error: Int) : Failure()

}