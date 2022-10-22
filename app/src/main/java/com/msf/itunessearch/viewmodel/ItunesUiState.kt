package com.msf.itunessearch.viewmodel

import com.msf.itunessearch.model.Music

sealed class ItunesUiState {
    data class Loading(val loading: Boolean) : ItunesUiState()
    data class Loaded(val musics: List<Music>) : ItunesUiState()
    data class Error(val message: String) : ItunesUiState()
    object Empty : ItunesUiState()
}
