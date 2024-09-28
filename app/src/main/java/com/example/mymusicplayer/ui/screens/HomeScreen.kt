package com.example.mymusicplayer.ui.screens

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mymusicplayer.R
import com.example.mymusicplayer.ui.Track
import com.example.mymusicplayer.ui.composables.SwipeAction
import com.example.mymusicplayer.ui.composables.SwipeContainer
import com.example.mymusicplayer.viewmodels.HomeVM
import com.google.android.exoplayer2.MediaItem

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun HomeScreen() {
    val viewModel: HomeVM = hiltViewModel()

    val tracks by viewModel.tracks.collectAsState()

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
                            painter = painterResource(id = R.drawable.queue),
                            contentDescription = "Queue Track Icon",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                )

                SwipeContainer(
                    item = item,
                    action = action,
                ) {
                    Surface(
                        modifier = Modifier
                            .clickable { viewModel.onTrackClick(index) }
                    ) {
                        Track(
                            mediaItem = tracks[index],
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                        )
                    }
                }
            }
        }
    }
}