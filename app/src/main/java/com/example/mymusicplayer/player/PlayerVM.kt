package com.example.mymusicplayer.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.example.mymusicplayer.player.playback.ExoPlayerWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.max

@HiltViewModel
class PlayerVM @Inject constructor(
    private val player: ExoPlayerWrapper
) : ViewModel(), ExoPlayerWrapper.ExoPlayerListener {

    private val _playerState = MutableStateFlow(player.playerState)
    val playerState: StateFlow<PlayerState> = _playerState

    private val _currentTrack = MutableStateFlow(player.currentTrack)
    val currentTrack: StateFlow<MediaItem?> = _currentTrack

    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition: StateFlow<Long> get() = _currentPosition

    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> get() = _duration

    override fun onPlayerStateChanged(playerState: PlayerState) {
        _playerState.value = playerState
    }

    override fun onTrackChanged(mediaItem: MediaItem) {
        _currentTrack.value = mediaItem
    }

    override fun onDurationChanged(duration: Long) {
        _duration.value = duration
    }

    override fun onPositionChanged(position: Long) {
        _currentPosition.value = position
    }

    init {
        _duration.value = max(0, player.getDuration())
        player.addListener(this)
        viewModelScope.launch {
            while (true) {
                _currentPosition.value = player.getPosition()
                delay(10)
            }
        }
    }

    fun onPlayPauseClick() {
        if (_playerState.value.isPlaying) {
            player.pause()
        } else {
            player.play()
        }
    }

    fun onSkipNextClick() {
        player.next()
        player.play()
    }

    fun onSkipPreviousClick() {
        player.previous()
        player.play()
    }

    fun onLoopClick() {
        player.toggleLooping()
    }

    fun onTypeClick() {
        player.nextType()
    }

    fun onShuffleClick() {
        if (_playerState.value.isShuffled) {
            player.unShuffle()
        } else {
            player.shuffle()
        }
    }

    fun onSeek(position: Long) {
        player.seekTo(position)
    }

    fun onDispose() {
        player.removeListener(this)
    }
}