package com.example.mymusicplayer.viewmodels

import androidx.lifecycle.ViewModel
import com.example.mymusicplayer.models.ExoPlayerWrapper
import com.example.mymusicplayer.models.PlayState
import com.google.android.exoplayer2.MediaItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MusicBarVM @Inject constructor(
    private val player: ExoPlayerWrapper
) : ViewModel() {

    val playState: StateFlow<PlayState> = player.playState
    val currentTrack: StateFlow<MediaItem?> = player.current

    fun onPlayPauseClick() {
        player.togglePlay()
    }

    fun onSkipNextClick() {
        player.next()
    }

    fun onSkipPreviousClick() {
        player.previous()
    }

    fun onLoopClick() {
        player.toggleLooping()
    }

    fun onTypeClick() {
        player.nextType()
    }
}