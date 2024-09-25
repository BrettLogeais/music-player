package com.example.mymusicplayer.models

data class PlayState(
    val playType: PlayType,
    val isPlaying: Boolean,
    val isLooping: Boolean
)

enum class PlayType {
    SINGLE,     // play single songs at a time
    ORDERED,    // continue playing songs in order
    SHUFFLE;    // continue playing songs randomly

    fun nextType(): PlayType {
        return when (this) {
            SINGLE -> ORDERED
            ORDERED -> SHUFFLE
            else -> SINGLE
        }
    }
}