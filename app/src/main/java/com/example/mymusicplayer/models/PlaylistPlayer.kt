package com.example.mymusicplayer.models

import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.LinkedList

class PlaylistPlayer {

    private val _mediaPlayer = MediaPlayer()

    private val _isPlaying = MutableLiveData<Boolean>()
    val isPlaying: LiveData<Boolean> get() = _isPlaying

    private val _current = MutableLiveData<AudioTrack>()
    val current: LiveData<AudioTrack> get() = _current

    private var _playlist: List<AudioTrack> = listOf()
    private var _position = 0

    init {
        _mediaPlayer.setOnPreparedListener {
            play()
        }
        _mediaPlayer.setOnCompletionListener {
            next()
        }
        _current.observeForever {
            start()
        }
    }

    fun play() {
        if (_mediaPlayer.isPlaying) return
        _isPlaying.postValue(true)
        _mediaPlayer.start()
    }

    fun pause() {
        if (!_mediaPlayer.isPlaying) return
        _isPlaying.postValue(false)
        _mediaPlayer.pause()
    }

    fun isPlaying(): Boolean = _mediaPlayer.isPlaying

    fun toggleLoop() {
        _mediaPlayer.isLooping = !_mediaPlayer.isLooping
    }

    private fun start() {
        val track = _current.value
        track?.let {
            _mediaPlayer.reset()
            _mediaPlayer.setDataSource(track.path)
            _mediaPlayer.prepare()
        }
    }

    fun playTrackFromPlaylist(index: Int, playlist: List<AudioTrack>) {
        _playlist = playlist
        _position = index
        _current.postValue(_playlist[_position])
    }

    fun next() {
        _position = (_position + 1) % _playlist.size
        _current.postValue(_playlist[_position])
    }

    fun previous() {
        _position = (_position + _playlist.size - 1) % _playlist.size
        _current.postValue(_playlist[_position])
    }
}