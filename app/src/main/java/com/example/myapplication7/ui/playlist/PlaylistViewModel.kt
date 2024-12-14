package com.example.myapplication7.ui.playlist
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlaylistViewModel : ViewModel() {
    private val _playlists = MutableLiveData<MutableList<String>>(mutableListOf())
    val playlists: LiveData<MutableList<String>> get() = _playlists

    fun addPlaylist(playlistName: String) {
        _playlists.value?.apply {
            add(playlistName)
            _playlists.value = this
        }
    }
}