package com.example.mymusicplayer.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import com.example.mymusicplayer.models.AudioTrack
import com.example.mymusicplayer.viewmodels.MusicBarVM

@Composable
fun Track(
    audioTrack: AudioTrack,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = audioTrack.title,
            modifier = Modifier
        )
        Text(
            text = audioTrack.artist,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.align(Alignment.TopEnd)
        )
    }
}

@Composable
@Preview
private fun Preview() {
    Track(AudioTrack("reign", "Re:gn", ""))
}