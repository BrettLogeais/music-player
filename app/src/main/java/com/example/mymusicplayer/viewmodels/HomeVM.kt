package com.example.mymusicplayer.viewmodels

import android.content.Context
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import com.example.mymusicplayer.models.ExoPlayerWrapper
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeVM @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val player: ExoPlayerWrapper
): ViewModel() {

    private val _tracks = MutableStateFlow<List<MediaItem>>(listOf())
    val tracks: StateFlow<List<MediaItem>> get() = _tracks

    init {
        getMusic()
    }

    private fun getMusic() {
        val tracks = mutableListOf<MediaItem>()

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

                    val mediaItem = MediaItem.Builder()
                        .setUri(path)
                        .setMediaMetadata(
                            MediaMetadata.Builder()
                                .setTitle(title)
                                .setArtist(artist)
                                .build()
                        )
                        .build()

                    tracks.add(mediaItem)
                } while (cursor.moveToNext())
            }
        }
        _tracks.value = tracks.sortedBy { it.mediaMetadata.title.toString() }
    }

    fun onTrackClick(index: Int) {
        player.playItemFromPlaylist(index, _tracks.value)
    }
}