package com.example.myapplication7.data.model

data class Playlist(
    val id: Int,
    val name: String,
    val tracks: MutableList<Data> // Replace `Track` with your model for tracks.
)