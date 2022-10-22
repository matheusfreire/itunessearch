package com.msf.itunessearch.repository

import com.msf.itunessearch.model.TracksResponse
import com.msf.itunessearch.network.ResultWrapper

interface ItunesSearchRepository {
    suspend fun fetchMusics(query: String, country: String): ResultWrapper<TracksResponse>
}
