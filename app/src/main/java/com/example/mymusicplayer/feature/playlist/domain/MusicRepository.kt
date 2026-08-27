package com.example.mymusicplayer.feature.playlist.domain

import com.example.mymusicplayer.feature.playlist.domain.model.RemoteTrack

interface MusicRepository {
    suspend fun getTracks(): List<RemoteTrack>
}