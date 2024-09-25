package com.example.mymusicplayer.models

import android.media.MediaPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class PlaylistPlayer {

    private val _mediaPlayer = MediaPlayer()


    private val _playState = MutableStateFlow(
        PlayState(
            playType = PlayType.SINGLE,
            isPlaying = false,
            isLooping = false
        )
    )
    val playState: StateFlow<PlayState> get() = _playState

    private val _current = MutableStateFlow<AudioTrack?>(null)
    val current: StateFlow<AudioTrack?> get() = _current

    private var _playlist: List<AudioTrack> = listOf()
    private var _position = 0

    init {
        _mediaPlayer.setOnPreparedListener {
            play()
        }
        _mediaPlayer.setOnCompletionListener {
            when (playState.value.playType) {
                PlayType.SINGLE -> { if (!playState.value.isLooping) pause() }
                else -> next()
            }
        }
    }

    fun play() {
        _mediaPlayer.start()
        _playState.update { it.copy(isPlaying = true) }
    }

    fun pause() {
        _mediaPlayer.pause()
        _playState.update { it.copy(isPlaying = false) }
    }

    fun togglePlay() {
        val isPlaying = !_mediaPlayer.isPlaying
        if (isPlaying) {
            _mediaPlayer.start()
        } else {
            _mediaPlayer.pause()
        }
        _playState.update { it.copy(isPlaying = isPlaying) }
    }

    fun toggleLooping() {
        val isLooping = !_mediaPlayer.isLooping
        _mediaPlayer.isLooping = isLooping
        _playState.update { it.copy(isLooping = isLooping) }
    }

    fun nextType() {
        val type = _playState.value.playType.nextType()
        // todo : randomize playlist
        // todo : exoplayer?
        _playState.update { it.copy(playType = type) }
    }

    private fun start() {
        _current.value?.let {
            _mediaPlayer.reset()
            _mediaPlayer.setDataSource(it.path)
            _mediaPlayer.prepare()
        }
    }

    fun playTrackFromPlaylist(index: Int, playlist: List<AudioTrack>) {
        _playlist = playlist
        _position = index
        _current.value = _playlist[_position]
        start()
    }

    fun next() {
        _position = (_position + 1) % _playlist.size
        _current.value = _playlist[_position]
        start()
    }

    fun previous() {
        _position = (_position + _playlist.size - 1) % _playlist.size
        _current.value = _playlist[_position]
        start()
    }
}