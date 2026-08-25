package com.example.mymusicplayer.models

data class PlayerState(
    val isPlaying: Boolean = false,
    val isPlayAll: Boolean = false,
    val isLooping: Boolean = false,
    val isShuffled: Boolean = false
)