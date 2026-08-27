package com.example.mymusicplayer.feature.playlist.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.DismissDirection
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import com.example.mymusicplayer.R
import com.example.mymusicplayer.feature.playlist.presentation.component.TrackView
import com.example.mymusicplayer.feature.playlist.presentation.component.SwipeAction
import com.example.mymusicplayer.feature.playlist.presentation.component.SwipeContainer

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun HomeScreen(
    viewModel: LocalTracksVM = hiltViewModel(),
) {
    val tracks by viewModel.tracks.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyColumn {
            itemsIndexed(tracks) { index, item ->
                val action = SwipeAction<MediaItem>(
                    direction = DismissDirection.StartToEnd,
                    action = { viewModel.onTrackSwipe(index) },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_queue),
                            contentDescription = "Queue Track Icon",
                            tint = MaterialTheme.colorScheme.onPrimary,
                        )
                    },
                )

                SwipeContainer(
                    item = item,
                    action = action,
                ) {
                    Surface(
                        modifier = Modifier.clickable { viewModel.onTrackClick(index) },
                    ) {
                        TrackView(
                            mediaItem = item,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                        )
                    }
                }
            }
        }
    }
}