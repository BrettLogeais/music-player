package com.example.mymusicplayer.feature.playlist.domain

import androidx.media3.common.MediaItem

interface DeviceMusicRepository {
    fun getTracks(): List<MediaItem>
}