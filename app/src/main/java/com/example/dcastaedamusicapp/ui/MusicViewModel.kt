package com.example.dcastaedamusicapp.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dcastaedamusicapp.data.Album
import com.example.dcastaedamusicapp.data.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MusicViewModel : ViewModel() {
    private val _albumsState = MutableStateFlow<UiState<List<Album>>>(UiState.Loading)
    val albumsState: StateFlow<UiState<List<Album>>> = _albumsState
    private val _albumDetailState = MutableStateFlow<UiState<Album>>(UiState.Loading)
    val albumDetailState: StateFlow<UiState<Album>> = _albumDetailState

    init {
        fetchAlbums()
    }

    fun fetchAlbums() {
        viewModelScope.launch {
            _albumsState.value = UiState.Loading
            try {
                _albumsState.value = UiState.Success(RetrofitInstance.api.getAlbums())
            } catch (e: Exception) {
                _albumsState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun fetchAlbumDetail(albumId: String) {
        viewModelScope.launch {
            _albumDetailState.value = UiState.Loading
            try {
                _albumDetailState.value = UiState.Success(RetrofitInstance.api.getAlbumDetail(albumId))
            } catch (e: Exception) {
                _albumDetailState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}