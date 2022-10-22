package com.msf.itunessearch.repository

import com.msf.itunessearch.network.ResultWrapper

interface ItunesSearchRepository {
    suspend fun fetchMusics(query: String): ResultWrapper<Any>
}
