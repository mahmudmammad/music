package com.example.myapplication7.ui.playlist
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication7.data.model.Data
import com.example.myapplication7.data.model.Playlist
class PlaylistViewModel : ViewModel() {

    private val _playlists = MutableLiveData<MutableList<Playlist>>(mutableListOf())
    val playlists: LiveData<MutableList<Playlist>> get() = _playlists

    // Add a new playlist
    fun addPlaylist(playlist: Playlist) {
        _playlists.value?.add(playlist)
        _playlists.value = _playlists.value // Trigger observers
    }
    fun updatePlaylist(updatedPlaylist: Playlist) {
        val currentPlaylists = _playlists.value ?: mutableListOf()
        val index = currentPlaylists.indexOfFirst { it.id == updatedPlaylist.id }
        if (index != -1) {
            currentPlaylists[index] = updatedPlaylist
            _playlists.value = currentPlaylists
        }
    }

    // Retrieve a playlist by its ID
    fun getPlaylistById(id: Int): Playlist? {
        return _playlists.value?.find { it.id == id }
    }

    // Add a track to a specific playlist
    fun addTrackToPlaylist(track: Data, playlistId: Int) {
        val currentPlaylists = _playlists.value ?: return
        val playlist = currentPlaylists.find { it.id == playlistId }
        playlist?.tracks?.add(track)
        _playlists.value = currentPlaylists // Trigger LiveData update
    }
}


