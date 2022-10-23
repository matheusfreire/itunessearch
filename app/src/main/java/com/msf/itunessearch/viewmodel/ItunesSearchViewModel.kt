package com.msf.itunessearch.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msf.itunessearch.model.Music
import com.msf.itunessearch.network.ResultWrapper
import com.msf.itunessearch.usecase.ItunesSearchUseCase

class ItunesSearchViewModel(
    private val useCase: ItunesSearchUseCase
) : ViewModel() {

    private val _uiStateLiveData = MutableLiveData<ItunesUiState>()
    val uiStateLiveData = _uiStateLiveData
    private var musics = emptyList<Music>()

    init {
        _uiStateLiveData.postValue(ItunesUiState.Empty)
    }

    fun fetchMusic(term: String, country: String) {
        val genericMessageError = "Something got wrong, please try again"
        _uiStateLiveData.postValue(ItunesUiState.Loading(true))
        useCase(
            scope = viewModelScope,
            params = Pair(term, country),
            onSuccess = { result ->
                _uiStateLiveData.postValue(ItunesUiState.Loading(false))
                when (result) {
                    is ResultWrapper.Success -> {
                        musics = result.value.musics
                        _uiStateLiveData.postValue(ItunesUiState.Loaded(musics))
                    }
                    is ResultWrapper.GenericError -> uiStateLiveData.postValue(
                        ItunesUiState.Error(result.error ?: genericMessageError)
                    )
                    else -> _uiStateLiveData.postValue(ItunesUiState.Empty)
                }
            },
            onError = {
                _uiStateLiveData.postValue(ItunesUiState.Loading(false))
                it.message?.let { message ->
                    _uiStateLiveData.postValue(ItunesUiState.Error(message))
                } ?: run {
                    _uiStateLiveData.postValue(ItunesUiState.Error(genericMessageError))
                }
            }
        )
    }

    fun getMusic(position: Int): Music? {
        if (_uiStateLiveData.value is ItunesUiState.Loaded) {
            return musics[position]
        }
        return null
    }
}
