package com.msf.itunessearch.network

import com.msf.itunessearch.model.TracksResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesService {
    @GET("?media=music&limit=200")
    suspend fun fetchMusics(
        @Query("term") track: String,
        @Query("country") country: String
    ): TracksResponse
}
