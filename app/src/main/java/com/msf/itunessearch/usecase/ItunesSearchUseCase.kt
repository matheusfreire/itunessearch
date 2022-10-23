package com.msf.itunessearch.usecase

import com.msf.itunessearch.core.CoroutinesContextProvider
import com.msf.itunessearch.core.Either
import com.msf.itunessearch.core.RequestWrapper
import com.msf.itunessearch.core.UseCase
import com.msf.itunessearch.model.Music
import com.msf.itunessearch.model.TracksResponse
import com.msf.itunessearch.network.ResultWrapper
import com.msf.itunessearch.repository.ItunesSearchRepository

class ItunesSearchUseCase(
    private val repository: ItunesSearchRepository,
    contextProvider: CoroutinesContextProvider,
    private val requestWrapper: RequestWrapper
) : UseCase<ResultWrapper<TracksResponse>, Pair<String, String>>(contextProvider) {
    override suspend fun run(params: Pair<String, String>): Either<ResultWrapper<TracksResponse>, Throwable> =
        requestWrapper.wrapper {
            when (val result = repository.fetchMusics(params.first, params.second)) {
                is ResultWrapper.Success -> handleSuccess(result)
                is ResultWrapper.GenericError -> result
                else -> ResultWrapper.NetworkError
            }
        }

    private fun handleSuccess(result: ResultWrapper.Success<TracksResponse>): ResultWrapper<TracksResponse> {
        val totalResults = result.value.resultCount
        return if (totalResults > 0) {
            ResultWrapper.Success(result.value)
        } else {
            ResultWrapper.GenericError(404, "No musics founded with your criteria")
        }
    }
}
