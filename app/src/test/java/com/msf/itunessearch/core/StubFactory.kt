package com.msf.itunessearch.core

import com.msf.itunessearch.model.Music
import com.msf.itunessearch.model.TracksResponse

object StubFactory {

    val tracksResponse =
        TracksResponse(
            1,
            listOf(
                Music(
                    artistId = 1,
                    artistName = "artistName",
                    artistViewUrl = "url",
                    artworkUrl100 = "artworkUrl100",
                    artworkUrl30 = "artworkUrl300",
                    artworkUrl60 = "artworkUrl60",
                    collectionCensoredName = "censoredName",
                    collectionExplicitness = "explicitNess",
                    collectionId = 1,
                    collectionName = "collectionName",
                    collectionPrice = 1.00,
                    collectionViewUrl = "collectionUrl",
                    country = "country",
                    currency = "currency",
                    discCount = 1,
                    discNumber = 1,
                    isStreamable = true,
                    kind = "kind",
                    previewUrl = "previewUrl",
                    primaryGenreName = "genreName",
                    releaseDate = "releaseDate",
                    trackCensoredName = "trackCensoredName",
                    trackCount = 1,
                    trackExplicitness = "explicitness",
                    trackId = 1,
                    trackName = "trackName",
                    trackNumber = 1,
                    trackPrice = 1.00,
                    trackTimeMillis = 1000,
                    trackViewUrl = "trackViewUrl",
                    wrapperType = "wrapperType"
                )
            )
        )
}
