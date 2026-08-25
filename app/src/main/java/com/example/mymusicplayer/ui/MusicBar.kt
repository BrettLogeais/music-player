package com.example.mymusicplayer.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mymusicplayer.R
import com.example.mymusicplayer.viewmodels.PlayerVM

@Preview
@Composable
private fun Preview() {
    Card {
        MusicBar()
    }
}

@Composable
fun MusicBar(
    modifier: Modifier = Modifier,
    playerVM: PlayerVM = hiltViewModel(),
    onTrackClick: () -> Unit = {}
) {
    val playerState by playerVM.playerState.collectAsState()
    val currentTrack by playerVM.currentTrack.collectAsState()

    val currentPosition by playerVM.currentPosition.collectAsState()
    val duration by playerVM.duration.collectAsState()

//    DisposableEffect(Unit) {
//        onDispose { playerVM.onDispose() }
//    }

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = modifier
    ) {
        Box {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp)
                        .align(Alignment.CenterVertically)
                        .clickable { onTrackClick() }
                ) {
                    currentTrack?.let {
                        Text(
                            text = it.mediaMetadata.title.toString(),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .align(Alignment.Start)
                        )
                        Text(
                            text = it.mediaMetadata.artist.toString(),
                            fontSize = 12.sp,
                            lineHeight = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .align(Alignment.Start)
                        )
                    }
                }

                IconButton(onClick = { playerVM.onSkipPreviousClick() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_skip_previous),
                        contentDescription = "Skip Previous Button"
                    )
                }

                IconButton(onClick = { playerVM.onPlayPauseClick() }) {
                    if (playerState.isPlaying) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_pause),
                            contentDescription = "Pause Audio Button"
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_play),
                            contentDescription = "Play Audio Button"
                        )
                    }
                }

                IconButton(onClick = { playerVM.onSkipNextClick() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_skip_next),
                        contentDescription = "Skip Next Button"
                    )
                }

                IconButton(onClick = { playerVM.onTypeClick() }) {
                    if (playerState.isPlayAll) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_state_all),
                            contentDescription = "Toggle Play All Button"
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_state_one),
                            contentDescription = "Toggle Play All Button"
                        )
                    }
                }

                IconButton(onClick = { playerVM.onLoopClick() }) {
                    if (playerState.isLooping) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_state_repeat_on),
                            contentDescription = "Toggle Loop Audio Button"
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_state_repeat_off),
                            contentDescription = "Toggle Loop Audio Button"
                        )
                    }
                }
            }

            LinearProgressIndicator(
                progress = {
                    if (duration > 0L) currentPosition.toFloat() / duration.toFloat()
                    else 0f
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .padding(horizontal = 12.dp)
                    .align(Alignment.BottomCenter)
            )
        }
    }
}