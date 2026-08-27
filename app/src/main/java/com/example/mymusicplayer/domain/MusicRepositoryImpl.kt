package com.example.mymusicplayer.domain

import com.example.mymusicplayer.data.remote.MusicApi
import com.example.mymusicplayer.data.remote.MusicRepository
import com.example.mymusicplayer.domain.models.RemoteTrack
import com.example.mymusicplayer.domain.models.toDomain

class MusicRepositoryImpl(
    private val api: MusicApi
) : MusicRepository {
    override suspend fun getTracks(): List<RemoteTrack> {
        val result = api.getTracks()
        return result.map { it.toDomain() }
    }
}