package com.msf.itunessearch.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msf.itunessearch.usecase.ItunesSearchUseCase

class ItunesSearchViewModel(
    private val useCase: ItunesSearchUseCase
) : ViewModel() {

    private val _uiStateLiveData = MutableLiveData<ItunesUiState>()
    val uiStateLiveData = _uiStateLiveData

    fun fetchMusic() {
        useCase(
            scope = viewModelScope,
            params = Pair("capital+inicial", "BR"),
            onSuccess = {
                it?.let { musics ->
                    if (musics.isNotEmpty()) {
                        _uiStateLiveData.postValue(ItunesUiState.Loaded(musics))
                    } else {
                        _uiStateLiveData.postValue(ItunesUiState.Empty)
                    }
                }
            },
            onError = {
                it.message?.let { message ->
                    _uiStateLiveData.postValue(ItunesUiState.Error(message))
                } ?: run {
                    _uiStateLiveData.postValue(ItunesUiState.Error("Something got wrong, please try again"))
                }
            }
        )
    }
}
