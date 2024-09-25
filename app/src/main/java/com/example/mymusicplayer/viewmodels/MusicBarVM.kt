package com.example.mymusicplayer.viewmodels

import androidx.lifecycle.ViewModel
import com.example.mymusicplayer.models.AudioTrack
import com.example.mymusicplayer.models.PlayState
import com.example.mymusicplayer.models.PlaylistPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MusicBarVM @Inject constructor(
    private val playlistPlayer: PlaylistPlayer
) : ViewModel() {

    val playState: StateFlow<PlayState> = playlistPlayer.playState
    val currentTrack: StateFlow<AudioTrack?> = playlistPlayer.current

    fun onPlayPauseClick() {
        playlistPlayer.togglePlay()
    }

    fun onSkipNextClick() {
        playlistPlayer.next()
    }

    fun onSkipPreviousClick() {
        playlistPlayer.previous()
    }

    fun onLoopClick() {
        playlistPlayer.toggleLooping()
    }

    fun onTypeClick() {
        playlistPlayer.nextType()
    }
}