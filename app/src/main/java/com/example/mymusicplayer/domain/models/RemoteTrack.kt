package com.example.mymusicplayer.domain.models

import androidx.compose.runtime.Immutable
import com.example.mymusicplayer.data.remote.dto.TrackDto

@Immutable
data class RemoteTrack(
    val name: String,
    val size: Long,
    val lastModified: String,
    val streamUrl: String,
)

fun TrackDto.toDomain(): RemoteTrack =
    RemoteTrack(
        name = name,
        size = size,
        lastModified = lastModified,
        streamUrl = streamUrl,
    )