package com.example.mymusicplayer.feature.playlist.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.example.mymusicplayer.di.BASE_URL
import com.example.mymusicplayer.feature.playlist.domain.MusicRepository
import com.example.mymusicplayer.player.playback.ExoPlayerWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RemoteTracksVM @Inject constructor(
    private val player: ExoPlayerWrapper,
    private val repository: MusicRepository,
): ViewModel() {

    private val _tracks = MutableStateFlow<List<MediaItem>>(listOf())
    val tracks = _tracks.asStateFlow()

    init {
        getRemoteTracks()
    }

    private fun getRemoteTracks() {
        viewModelScope.launch {
            try {
                val result = repository.getTracks()
                _tracks.value = result.map {
                    val url = BASE_URL.trimEnd('/') + it.streamUrl
                    MediaItem.Builder()
                        .setUri(url)
                        .setMediaMetadata(
                            MediaMetadata.Builder()
                                .setTitle(it.name)
                                .build()
                        )
                        .build()
                }
            } catch (e: Exception) {
                println(e)
            }
        }
    }

    fun onTrackClick(index: Int) {
        player.playItemFromPlaylist(index, _tracks.value)
    }

    fun onTrackSwipe(index: Int) {
        val track = _tracks.value[index]
        player.queueItem(track)
    }
}