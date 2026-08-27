package com.example.mymusicplayer.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class TrackDto(
    val name: String,
    val size: Long,
    val lastModified: String,
    val streamUrl: String,
)