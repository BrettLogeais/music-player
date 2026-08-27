package com.example.mymusicplayer.feature.playlist.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.media3.common.MediaItem

@Composable
fun TrackView(
    mediaItem: MediaItem,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = mediaItem.mediaMetadata.title.toString(),
            modifier = Modifier
        )
    }
}