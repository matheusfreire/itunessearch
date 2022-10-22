package com.msf.itunessearch.core

interface RequestWrapper {
    suspend fun <D> wrapper(call: suspend () -> D): Either<D, Throwable>
}
