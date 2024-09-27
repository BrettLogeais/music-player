package com.example.mymusicplayer.viewmodels

import android.content.Context
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymusicplayer.models.ExoPlayerWrapper
import com.example.mymusicplayer.models.PlayMode
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.Player
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

@HiltViewModel
class TrackVM @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val player: ExoPlayerWrapper
): ViewModel() {

    val currentTrack: StateFlow<MediaItem?> = player.current

    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition: StateFlow<Long> get() = _currentPosition

    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> get() = _duration

    init {
        _duration.value = max(0, player.getDuration())
        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                _duration.value = max(0, player.getDuration())
            }
        })
        viewModelScope.launch {
            while (true) {
                _currentPosition.value = player.getPosition()
                delay(50)
            }
        }
    }

    fun onSeek(position: Long) {
        player.seekTo(position)
    }

}