package com.example.mymusicplayer.viewmodels

import android.content.Context
import android.media.MediaPlayer
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import com.example.mymusicplayer.models.AudioTrack
import com.example.mymusicplayer.models.PlaylistPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeVM @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val playlistPlayer: PlaylistPlayer
): ViewModel() {

    private val _tracks = MutableStateFlow<List<AudioTrack>>(listOf())
    val tracks: StateFlow<List<AudioTrack>> get() = _tracks

    init {
        getMusic()
    }

    private fun getMusic() {
        val tracks = mutableListOf<AudioTrack>()

        val cResolver = appContext.contentResolver
        val songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        cResolver.query(songUri, null, null, null, null).use { cursor ->
            if (cursor != null && cursor.moveToFirst()) {
                val titleIndex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
                val artistIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
                val uriIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA)
                do {
                    val title = cursor.getString(titleIndex)
                    val artist = cursor.getString(artistIndex)
                    val path = cursor.getString(uriIndex)
                    tracks.add(AudioTrack(title, artist, path))
                } while (cursor.moveToNext())
            }
        }
        _tracks.value = tracks.sortedBy { it.title }
    }

    fun onTrackClick(index: Int) {
        playlistPlayer.playTrackFromPlaylist(index, _tracks.value)
    }
}