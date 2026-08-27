package com.example.mymusicplayer.feature.playlist.presentation

import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import com.example.mymusicplayer.feature.playlist.domain.DeviceMusicRepository
import com.example.mymusicplayer.player.playback.ExoPlayerWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LocalTracksVM @Inject constructor(
    private val player: ExoPlayerWrapper,
    private val repository: DeviceMusicRepository,
): ViewModel() {

    private val _tracks = MutableStateFlow<List<MediaItem>>(listOf())
    val tracks = _tracks.asStateFlow()

    init {
        getMusic()
    }

    private fun getMusic() {
        _tracks.value = repository.getTracks()
    }

    fun onTrackClick(index: Int) {
        player.playItemFromPlaylist(index, _tracks.value)
    }

    fun onTrackSwipe(index: Int) {
        player.queueItem(_tracks.value[index])
    }
}