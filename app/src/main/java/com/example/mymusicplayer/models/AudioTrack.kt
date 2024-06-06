package com.example.mymusicplayer.models

import androidx.compose.runtime.Immutable

@Immutable
data class AudioTrack(
    val title: String,
    val artist: String,
    val path: String
)