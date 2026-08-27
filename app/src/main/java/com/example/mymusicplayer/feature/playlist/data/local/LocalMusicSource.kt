package com.example.mymusicplayer.feature.playlist.data.local

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LocalMusicSource @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    fun getTracks(): List<MediaItem> {
        val tracks = mutableListOf<MediaItem>()

        val contentResolver = context.contentResolver
        val songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        contentResolver.query(
            songUri,
            null,
            null,
            null,
            null
        )?.use { cursor ->

            val titleIndex =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)

            val artistIndex =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)

            val idIndex =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idIndex)
                val title = cursor.getString(titleIndex)
                val artist = cursor.getString(artistIndex)

                val uri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                tracks += MediaItem.Builder()
                    .setMediaId(id.toString())
                    .setUri(uri)
                    .setMediaMetadata(
                        MediaMetadata.Builder()
                            .setTitle(title)
                            .setArtist(artist)
                            .build()
                    )
                    .build()
            }
        }

        return tracks.sortedBy {
            it.mediaMetadata.title?.toString().orEmpty()
        }
    }
}