package com.example.mymusicplayer.feature.playlist.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class AudioTrack(
    val title: String,
    val artist: String,
    val path: String
)