package com.example.mymusicplayer.feature.playlist.data.repository

import com.example.mymusicplayer.feature.playlist.data.remote.MusicApi
import com.example.mymusicplayer.feature.playlist.domain.MusicRepository
import com.example.mymusicplayer.feature.playlist.domain.model.RemoteTrack
import com.example.mymusicplayer.feature.playlist.domain.model.toDomain
import javax.inject.Inject

class MusicRepositoryImpl @Inject constructor(
    private val api: MusicApi
) : MusicRepository {
    override suspend fun getTracks(): List<RemoteTrack> {
        val result = api.getTracks()
        return result.map { it.toDomain() }
    }
}