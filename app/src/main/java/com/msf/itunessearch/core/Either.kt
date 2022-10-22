package com.msf.itunessearch.core

sealed class Either<out S, out F> {

    data class Failure<out F>(val failure: F) : Either<Nothing, F>()

    data class Success<out S>(val success: S) : Either<S, Nothing>()

    val isSuccess get() = this is Success<S>
    val isFailure get() = this is Failure<F>

    fun <F> failure(a: F) = Failure(a)
    fun <S> success(b: S) = Success(b)

    fun either(onSuccess: (S) -> Any, onFailure: (F) -> Any): Any = when (this) {
        is Failure -> onFailure(failure)
        is Success -> onSuccess(success)
    }
}
