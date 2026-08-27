package com.example.mymusicplayer.feature.playlist.domain.model

import androidx.compose.runtime.Immutable
import com.example.mymusicplayer.feature.playlist.data.remote.dto.TrackDto

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