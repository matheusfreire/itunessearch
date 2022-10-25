package com.msf.itunessearch.core

import retrofit2.HttpException
import java.io.IOException

class RequestWrapperImpl : RequestWrapper {
    override suspend fun <D> wrapper(call: suspend () -> D): Either<D, Throwable> {
        return try {
            val dataFromRemote = call()
            Either.Success(dataFromRemote)
        } catch (httpException: HttpException) {
            return Either.Failure(httpException)
        } catch (ioException: IOException) {
            Either.Failure(ioException)
        } catch (stateException: IllegalStateException) {
            Either.Failure(stateException)
        } catch (e: Exception) {
            Either.Failure(e)
        }
    }
}
