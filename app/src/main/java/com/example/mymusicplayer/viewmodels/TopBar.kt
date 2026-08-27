package com.example.mymusicplayer.viewmodels

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mymusicplayer.Home
import com.example.mymusicplayer.R
import com.example.mymusicplayer.Remote

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavController,
    playerVM: PlayerVM = hiltViewModel(),
    onMenuClick: () -> Unit,
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val destination = backStackEntry?.destination

    val title = when {
        destination?.hasRoute<Home>() == true -> "Local"
        destination?.hasRoute<Remote>() == true -> "Remote"
        else -> ""
    }

    TopAppBar(
        title = {
            Text(text = title)
        },
        navigationIcon = {
            IconButton(
                onClick = onMenuClick
            ) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Navigation Menu Button"
                )
            }
        },
        actions = {
            val playState by playerVM.playerState.collectAsState()

            IconButton(
                onClick = { playerVM.onShuffleClick() }
            ) {
                Icon(
                    painter = painterResource(
                        when (playState.isShuffled) {
                            true -> R.drawable.ic_state_shuffle_on
                            false -> R.drawable.ic_state_shuffle_off
                        }
                    ),
                    contentDescription = "Shuffle Playlist Button"
                )
            }
        }
    )
}