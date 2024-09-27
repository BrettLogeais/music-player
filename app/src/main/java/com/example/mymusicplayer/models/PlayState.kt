package com.example.mymusicplayer.models

data class PlayState(
    val mode: PlayMode,
    val isPlaying: Boolean
)

enum class PlayMode {
    ONE,
    ONE_REPEAT,
    ALL,
    ALL_REPEAT;

    fun toggleModeType(): PlayMode {
        return when (this) {
            ONE -> ALL
            ONE_REPEAT -> ALL_REPEAT
            ALL -> ONE
            ALL_REPEAT -> ONE_REPEAT
        }
    }

    fun toggleModeRepeat(): PlayMode {
        return when (this) {
            ONE -> ONE_REPEAT
            ONE_REPEAT -> ONE
            ALL -> ALL_REPEAT
            ALL_REPEAT -> ALL
        }
    }
}