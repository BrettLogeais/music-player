package com.example.mymusicplayer.viewmodels

import android.content.Context
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.mymusicplayer.MusicApp
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeVM @Inject constructor(@ApplicationContext private val appContext: Context): ViewModel() {

    private val _songs = MutableStateFlow<List<String>>(listOf())
    val songs: StateFlow<List<String>> get() = _songs

    init {
        getMusic()
    }

    private fun getMusic() {

        val cResolver = appContext.contentResolver
        val songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        cResolver.query(songUri, null, null, null, null).use { cursor ->
            if (cursor != null && cursor.moveToFirst()) {
                val titleIndex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
                val artistIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
                do {
                    val title = cursor.getString(titleIndex)
                    val artist = cursor.getString(artistIndex)
                    _songs.update {
                        it + "$title | $artist"
                    }
                } while (cursor.moveToNext())
            }
        }
    }
}