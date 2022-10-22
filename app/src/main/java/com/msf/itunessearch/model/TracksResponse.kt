package com.msf.itunessearch.model

import com.google.gson.annotations.SerializedName

data class TracksResponse(
    val resultCount: Int,
    @SerializedName("results")
    val musics: List<Music>
)
