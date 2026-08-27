package com.example.mymusicplayer.feature.playlist.data.repository

import androidx.media3.common.MediaItem
import com.example.mymusicplayer.feature.playlist.data.local.LocalMusicSource
import com.example.mymusicplayer.feature.playlist.domain.DeviceMusicRepository
import javax.inject.Inject

class DeviceMusicRepositoryImpl @Inject constructor(
    private val localMusicSource: LocalMusicSource,
): DeviceMusicRepository {

    override fun getTracks(): List<MediaItem> {
        return localMusicSource.getTracks()
    }
}