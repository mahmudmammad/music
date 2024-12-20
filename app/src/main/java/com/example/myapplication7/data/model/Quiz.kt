package com.example.myapplication7.data.model

data class Quiz(
    val id: Int,
    val name: String,
    val playlist: Playlist, // Associated Playlist
    val isRandom: Boolean // True if questions are selected randomly
)

