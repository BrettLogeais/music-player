package com.example.mymusicplayer.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mymusicplayer.viewmodels.PlayerVM

@Composable
fun TrackScreen(
    playerVM: PlayerVM = hiltViewModel()
) {

    val track by playerVM.currentTrack.collectAsState()

    val currentPosition by playerVM.currentPosition.collectAsState()
    val duration by playerVM.duration.collectAsState()

    val maxDuration = if (duration > 0) duration else 1L

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        track?.let {
            Text(text = it.mediaMetadata.title.toString())
            Text(text = it.mediaMetadata.artist.toString())
        }
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            val currentMins = (currentPosition / 60000)
            val currentSecs = (currentPosition % 60000) / 1000
            val current = "$currentMins:${currentSecs.toString().padStart(2, '0')}"

            val lengthMins = (duration / 60000)
            val lengthSecs = (duration % 60000) / 1000
            val length = "$lengthMins:${lengthSecs.toString().padStart(2, '0')}"

            Slider(
                value = currentPosition.toFloat(),
                onValueChange = { playerVM.onSeek(it.toLong()) },
                valueRange = 0f..maxDuration.toFloat(),
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = current)
                Text(text = length)
            }
        }
    }
}