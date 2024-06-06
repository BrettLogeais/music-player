package com.example.mymusicplayer.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.mymusicplayer.R
import com.example.mymusicplayer.viewmodels.MusicBarVM

@Composable
fun MusicBar(viewModel: MusicBarVM) {
    BottomAppBar {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row {
                val isPlaying by viewModel.isPlaying.observeAsState(false)
                val current by viewModel.current.observeAsState()
                current?.let {
                    Text(text = it.title)
                }
                IconButton(onClick = { viewModel.onSkipPreviousClick() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.skip_previous),
                        contentDescription = "Play Audio Button"
                    )
                }
                IconButton(onClick = { viewModel.onPlayPauseClick() }) {
                    if (isPlaying) {
                        Icon(
                            painter = painterResource(id = R.drawable.pause),
                            contentDescription = "Pause Audio Button"
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Filled.PlayArrow,
                            contentDescription = "Play Audio Button"
                        )
                    }
                }
                IconButton(onClick = { viewModel.onSkipNextClick() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.skip_next),
                        contentDescription = "Play Audio Button"
                    )
                }
            }
        }
    }
}