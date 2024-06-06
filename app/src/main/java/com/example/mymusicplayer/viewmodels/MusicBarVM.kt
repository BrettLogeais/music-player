package com.example.mymusicplayer.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mymusicplayer.models.AudioTrack
import com.example.mymusicplayer.models.PlaylistPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MusicBarVM @Inject constructor(
    private val playlistPlayer: PlaylistPlayer
) : ViewModel() {

    val isPlaying: LiveData<Boolean> get() = playlistPlayer.isPlaying
    val current: LiveData<AudioTrack> get() = playlistPlayer.current

    fun onPlayPauseClick() {
        if (playlistPlayer.isPlaying()) {
            playlistPlayer.pause()
        } else {
            playlistPlayer.play()
        }
    }

    fun onSkipNextClick() {
        playlistPlayer.next()
    }

    fun onSkipPreviousClick() {
        playlistPlayer.previous()
    }
}