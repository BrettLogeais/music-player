package com.example.mymusicplayer.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.mymusicplayer.ui.Track
import com.example.mymusicplayer.viewmodels.HomeVM

@Composable
fun HomeScreen(viewModel: HomeVM) {
    val tracks by viewModel.tracks.collectAsState()

    val navController = rememberNavController()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(
                count = tracks.size,
                itemContent = { index ->
                    if (index > 0) {
                        HorizontalDivider(thickness = 1.dp)
                    }
                    Box(
                        modifier = Modifier.clickable { viewModel.onTrackClick(index) }
                    ) {
                        Track(
                            audioTrack = tracks[index],
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                        )
                    }
                }
            )
        }
        Button(
            onClick = {

            }
        ) {
            Text(text = "Play")
        }
    }
}