package com.example.mymusicplayer.data.remote

import com.example.mymusicplayer.domain.models.RemoteTrack

interface MusicRepository {
    suspend fun getTracks(): List<RemoteTrack>
}