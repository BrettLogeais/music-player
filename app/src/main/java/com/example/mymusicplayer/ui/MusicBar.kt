package com.example.mymusicplayer.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mymusicplayer.R
import com.example.mymusicplayer.models.PlayState
import com.example.mymusicplayer.models.PlayMode
import com.example.mymusicplayer.viewmodels.MusicBarVM
import com.google.android.exoplayer2.MediaItem

@Preview
@Composable
private fun Preview() {
    Bar(
        playState = PlayState(
            mode = PlayMode.ONE,
            isPlaying = false
        ),
        currentTrack = MediaItem.fromUri("")
    )
}

@Composable
fun MusicBar(
    onTrackClick: () -> Unit = {}
) {
    val viewModel: MusicBarVM = hiltViewModel()

    val playState by viewModel.playState.collectAsState()
    val current by viewModel.currentTrack.collectAsState()

    Bar(
        playState = playState,
        currentTrack = current,
        onTrackClick = onTrackClick,
        onTogglePlayback = { viewModel.onPlayPauseClick() },
        onPreviousClick = { viewModel.onSkipPreviousClick() },
        onNextClick = { viewModel.onSkipNextClick() },
        onTypeClick = { viewModel.onTypeClick() },
        onLoopClick = { viewModel.onLoopClick() }
    )
}

@Composable
private fun Bar(
    playState: PlayState,
    currentTrack: MediaItem?,
    onTrackClick: () -> Unit = {},
    onTogglePlayback: () -> Unit = {},
    onPreviousClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
    onTypeClick: () -> Unit = {},
    onLoopClick: () -> Unit = {}
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
                .align(Alignment.CenterVertically)
                .clickable { onTrackClick() }
        ) {
            currentTrack?.let {
                Text(
                    text = it.mediaMetadata.title.toString(),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .align(Alignment.Start)
                )
                Text(
                    text = it.mediaMetadata.artist.toString(),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .align(Alignment.Start)
                )
            }
        }

        IconButton(onClick = onPreviousClick) {
            Icon(
                painter = painterResource(id = R.drawable.skip_previous),
                contentDescription = "Play Audio Button"
            )
        }

        IconButton(onClick = onTogglePlayback) {
            if (playState.isPlaying) {
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

        IconButton(onClick = onNextClick) {
            Icon(
                painter = painterResource(id = R.drawable.skip_next),
                contentDescription = "Play Audio Button"
            )
        }

        IconButton(onClick = onTypeClick) {
            Icon(
                painter = painterResource(
                    id = when (playState.mode) {
                        PlayMode.ONE, PlayMode.ONE_REPEAT ->
                            R.drawable.single
                        PlayMode.ALL, PlayMode.ALL_REPEAT ->
                            R.drawable.ordered
                    }
                ),
                contentDescription = "Play Type Button"
            )
        }

        IconButton(onClick = onLoopClick) {
            Icon(
                painter = painterResource(
                    id = when (playState.mode) {
                        PlayMode.ONE_REPEAT, PlayMode.ALL_REPEAT ->
                            R.drawable.repeat_on
                        PlayMode.ONE, PlayMode.ALL ->
                            R.drawable.repeat_off
                    }
                ),
                contentDescription = "Loop Audio Button"
            )
        }
    }
}