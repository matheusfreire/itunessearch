package com.msf.itunessearch.repository.impl

import com.google.gson.Gson
import com.msf.itunessearch.model.TracksResponse
import com.msf.itunessearch.network.ItunesService
import com.msf.itunessearch.network.ResultWrapper
import com.msf.itunessearch.repository.ItunesSearchRepository
import retrofit2.HttpException

class ItunesSearchRepositoryImpl(private val itunesService: ItunesService) : ItunesSearchRepository {
    override suspend fun fetchMusics(query: String, country: String): ResultWrapper<TracksResponse> {
        return try {
            val fetchTracks = itunesService.fetchMusics(query, country)
            ResultWrapper.Success(fetchTracks)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val error = Gson().fromJson(errorBody, Error::class.java)
            ResultWrapper.GenericError(e.code(), error)
        }
    }
}
