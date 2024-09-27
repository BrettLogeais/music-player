package com.example.mymusicplayer.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import com.google.android.exoplayer2.MediaItem

@Composable
fun Track(
    mediaItem: MediaItem,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = mediaItem.mediaMetadata.title.toString(),
            modifier = Modifier
        )
        Text(
            text = mediaItem.mediaMetadata.artist.toString(),
            fontStyle = FontStyle.Italic,
            modifier = Modifier.align(Alignment.TopEnd)
        )
    }
}