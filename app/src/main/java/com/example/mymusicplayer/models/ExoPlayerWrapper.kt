package com.example.mymusicplayer.models

import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import javax.inject.Inject

/**
 * Wrapper for ExoPlayer for custom playlisting
 */

class ExoPlayerWrapper @Inject constructor(
    val player: ExoPlayer
) {

    var playerState = PlayerState()

    private var isPlayingQueue = false

    private var _items: List<MediaItem> = listOf()
    private var _order: List<Int> = listOf()
    private var _queue: MutableList<MediaItem> = mutableListOf()

    private var _index = -1
    var currentTrack: MediaItem? = null

    private val listeners = mutableListOf<ExoPlayerListener>()

    interface ExoPlayerListener {
        fun onPlayerStateChanged(playerState: PlayerState)
        fun onTrackChanged(mediaItem: MediaItem)
        fun onDurationChanged(duration: Long)
        fun onPositionChanged(position: Long)
    }

    fun addListener(listener: ExoPlayerListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: ExoPlayerListener) {
        listeners.remove(listener)
    }

    private fun notifyPlayerStateChanged(playerState: PlayerState) {
        listeners.forEach { it.onPlayerStateChanged(playerState) }
    }

    private fun notifyTrackChanged(mediaItem: MediaItem) {
        listeners.forEach { it.onTrackChanged(mediaItem) }
    }

    private fun notifyDurationChanged(duration: Long) {
        listeners.forEach { it.onDurationChanged(duration) }
    }

    private fun notifyPositionChanged(duration: Long) {
        listeners.forEach { it.onPositionChanged(duration) }
    }

    init {
        player.addListener(object : Player.Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            }

            override fun onPlaybackStateChanged(state: Int) {
                when (state) {
                    Player.STATE_READY -> {
                        println("Player Ready")
                        notifyPlayerStateChanged(playerState)
                        notifyDurationChanged(player.duration)
                    }
                    Player.STATE_BUFFERING -> {
                        println("Player Buffering")
                    }
                    Player.STATE_IDLE -> {
                        println("Player Idle")
                    }
                    Player.STATE_ENDED -> {
                        println("Playlist Ended")
                        when {
                            _queue.isNotEmpty() ->
                                next()
                            playerState.isLooping ->
                                next()
                            playerState.isPlayAll && _index < _items.size - 1 ->
                                next()
                            else -> pause()
                        }
                    }
                }
            }

            override fun onPositionDiscontinuity(
                oldPosition: Player.PositionInfo,
                newPosition: Player.PositionInfo,
                reason: Int
            ) {
                if (reason == Player.DISCONTINUITY_REASON_AUTO_TRANSITION) {
                    notifyPositionChanged(newPosition.positionMs)
                }
            }
        })
    }

    private fun restart() {
        player.prepare()
        if (!playerState.isPlaying) {
            if (playerState.isPlayAll) {
                seekTo(0)
            } else {
                seekTo(C.TIME_UNSET)
            }
        }
    }

    fun play() {
        if (player.playbackState == Player.STATE_ENDED) {
            restart()
        }
        player.play()

        playerState = playerState.copy(isPlaying = true)
        notifyPlayerStateChanged(playerState)
    }

    fun pause() {
        player.pause()

        playerState = playerState.copy(isPlaying = false)
        notifyPlayerStateChanged(playerState)
    }

    fun stop() {
        player.stop()
    }

    fun toggleLooping() {
        updatePlayMode(playerState.copy(isLooping = !playerState.isLooping))
    }

    fun nextType() {
        updatePlayMode(playerState.copy(isPlayAll = !playerState.isPlayAll))
    }

    private fun updatePlayMode(playerState: PlayerState) {
        when {
            !playerState.isPlayAll && playerState.isLooping ->
                player.repeatMode = Player.REPEAT_MODE_ONE
            else ->
                player.repeatMode = Player.REPEAT_MODE_OFF
        }

        this.playerState = playerState
        notifyPlayerStateChanged(playerState)
    }

    fun shuffle() {
        if (_items.isEmpty()) return
        if (_order.isEmpty()) _order = _items.indices.toList()
        val current = _order[_index]

        val indices = (_items.indices)
            .toMutableList()
            .apply { remove(current) }
            .shuffled()

        _order = mutableListOf(current).apply { addAll(indices) }
        _index = 0

        playerState = playerState.copy(isShuffled = true)
        notifyPlayerStateChanged(playerState)
    }

    fun unShuffle() {
        if (_items.isEmpty()) return
        if (_order.isEmpty()) _order = _items.indices.toList()
        val current = _order[_index]
        _order = _items.indices.toList()
        _index = current

        playerState = playerState.copy(isShuffled = false)
        notifyPlayerStateChanged(playerState)
    }

    fun playItemFromPlaylist(index: Int, mediaItems: List<MediaItem>) {
        setMediaItems(mediaItems)
        seekTo(index)
        if (playerState.isShuffled)
            shuffle()
        play()
    }

    fun queueItem(mediaItem: MediaItem) {
        _queue.add(mediaItem)
    }

    fun setMediaItems(mediaItems: List<MediaItem>) {
        _items = mediaItems
        _order = _items.indices.toList()
        player.prepare()
    }

    fun getMediaItem(index: Int): MediaItem? {
        if (_items.isEmpty() || _items.size != _order.size) return null
        if (index !in _items.indices) return null

        return _items[_order[index]]
    }

    fun getDuration(): Long {
        return player.duration
    }

    fun getPosition(): Long {
        return player.currentPosition
    }

    fun seekTo(mediaItemIndex: Int, positionMs: Long = C.TIME_UNSET) {
        if (mediaItemIndex !in _items.indices) return

        val mediaItem = getMediaItem(mediaItemIndex)
        mediaItem?.let {
            player.setMediaItem(it)
            player.seekTo(positionMs)
            currentTrack = it
            _index = mediaItemIndex

            notifyTrackChanged(it)
        }
    }

    fun seekTo(positionMs: Long = C.TIME_UNSET) {
        player.seekTo(positionMs)
    }

    fun next() {
        if (_items.isEmpty()) return
        if (_queue.isNotEmpty()) {
            isPlayingQueue = true
            val mediaItem = _queue[0]
            _queue.removeAt(0)
            player.setMediaItem(mediaItem)
            currentTrack = mediaItem

            notifyTrackChanged(mediaItem)
            play()
        } else {
            isPlayingQueue = false
            seekTo(nextIndex())
        }
    }

    private fun nextIndex(): Int {
        if (_items.size <= 1) return -1
        return (_index + 1) % _items.size
    }

    fun previous() {
        if (_items.isEmpty()) return
        if (isPlayingQueue) {
            isPlayingQueue = false
            seekTo(_index)
        } else {
            seekTo(previousIndex())
        }
    }

    private fun previousIndex(): Int {
        if (_items.size <= 1) return -1
        return (_index + _items.size - 1) % _items.size
    }
}