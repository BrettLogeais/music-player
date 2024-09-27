package com.example.mymusicplayer.models

import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * Wrapper For ExoPlayer for custom playlisting
 */

class ExoPlayerWrapper @Inject constructor(
    val player: ExoPlayer
) {

    private val _playState = MutableStateFlow(
        PlayState(
            mode = PlayMode.ONE,
            isPlaying = false
        )
    )
    val playState: StateFlow<PlayState> get() = _playState

    private val _current = MutableStateFlow<MediaItem?>(null)
    val current: StateFlow<MediaItem?> get() = _current

    private var _items: List<MediaItem> = listOf()
    private var _position = 0

    init {
        player.addListener(object : Player.Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                mediaItem?.let { _current.value = it }
            }

            override fun onPlaybackStateChanged(state: Int) {
                when (state) {
                    Player.STATE_READY -> {
                        println("Player Ready")
                    }
                    Player.STATE_BUFFERING -> {
                        println("Player Buffering")
                    }
                    Player.STATE_IDLE -> {
                        println("Player Idle")
                    }
                    Player.STATE_ENDED -> {
                        println("Playlist Ended")
                        when (_playState.value.mode) {
                            PlayMode.ALL -> if (_position < _items.size-1) next() else pause()
                            PlayMode.ALL_REPEAT -> next()
                            else -> pause()
                        }
                    }
                }
            }
        })
    }

    fun play() {
        player.play()
        _playState.update { it.copy(isPlaying = true) }
    }

    fun pause() {
        player.pause()
        _playState.update { it.copy(isPlaying = false) }
    }

    fun togglePlay() {
        val isPlaying = !player.isPlaying
        if (isPlaying) play()
        else pause()
    }

    fun toggleLooping() {
        val mode = _playState.value.mode.toggleModeRepeat()
        updatePlayState(_playState.value.copy(mode = mode))
    }

    fun nextType() {
        val mode = _playState.value.mode.toggleModeType()
        updatePlayState(_playState.value.copy(mode = mode))
    }

    private fun updatePlayState(playState: PlayState) {
        when (playState.mode) {
            PlayMode.ONE_REPEAT -> player.repeatMode = Player.REPEAT_MODE_ONE
            else -> player.repeatMode = Player.REPEAT_MODE_OFF
        }
        _playState.value = playState
    }

    private fun start() {
        _current.value?.let {
            player.prepare()
            play()
        }
    }

    fun setMediaItems(mediaItems: List<MediaItem>) {
        _items = mediaItems
    }

    fun seekTo(mediaItemIndex: Int, positionMs: Long = C.TIME_UNSET) {
        if (mediaItemIndex !in _items.indices) return
        if (mediaItemIndex == _position) {
            player.seekTo(positionMs)
        } else {
            val mediaItem = _items[mediaItemIndex]
            player.setMediaItem(mediaItem)
            player.seekTo(positionMs)
            _current.value = mediaItem
            _position = mediaItemIndex
        }
        start()
    }

    fun next() {
        if (_items.isEmpty()) return

        val position = nextPosition()
        if (position < 0) return

        val mediaItem = _items[position]
        player.setMediaItem(mediaItem)
        _current.value = mediaItem
        _position = position
        start()
    }

    private fun nextPosition(): Int {
        if (_items.size <= 1) return -1
        return (_position + 1) % _items.size
    }

    fun previous() {
        if (_items.isEmpty()) return

        if (player.currentPosition > 5000L) {
            player.seekTo(C.TIME_UNSET)
        } else {
            val position = previousPosition()
            if (position < 0) return

            val mediaItem = _items[position]
            player.setMediaItem(mediaItem)
            _current.value = mediaItem
            _position = position
            start()
        }
    }

    private fun previousPosition(): Int {
        if (_items.size <= 1) return -1
        return (_position + _items.size - 1) % _items.size
    }
}