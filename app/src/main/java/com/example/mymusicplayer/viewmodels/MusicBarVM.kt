package com.example.mymusicplayer.viewmodels

import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MusicBarVM @Inject constructor(
    private val mediaPlayer: MediaPlayer
) : ViewModel() {

    fun togglePlayback() {
        when {
            mediaPlayer.isPlaying -> mediaPlayer.pause()
            else -> mediaPlayer.start()
        }
    }

    fun toggleLoop() {
        mediaPlayer.isLooping = !mediaPlayer.isLooping
    }
}