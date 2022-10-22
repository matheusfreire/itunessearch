package com.msf.itunessearch.repository.impl

import com.msf.itunessearch.network.ItunesService
import com.msf.itunessearch.network.ResultWrapper
import com.msf.itunessearch.repository.ItunesSearchRepository

class ItunesSearchRepositoryImpl(private val itunesService: ItunesService) : ItunesSearchRepository {
    override suspend fun fetchMusics(query: String): ResultWrapper<Any> {
        return ResultWrapper.NetworkError
    }
}
