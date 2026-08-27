package com.example.mymusicplayer

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.compose.AppTheme
import com.example.mymusicplayer.player.playback.ExoPlayerWrapper
import com.example.mymusicplayer.player.service.PlayerService
import com.example.mymusicplayer.feature.track.presentation.MusicBar
import com.example.mymusicplayer.feature.playlist.presentation.HomeScreen
import com.example.mymusicplayer.feature.playlist.presentation.RemoteTracksScreen
import com.example.mymusicplayer.feature.track.TrackScreen
import com.example.mymusicplayer.core.ui.component.TopBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var player: ExoPlayerWrapper

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.entries.forEach {
            Log.d("DEBUG", "${it.key} = ${it.value}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launchPermissionRequest()

        startService()

        setContent {
            val navController = rememberNavController()

            val drawerState = rememberDrawerState(
                initialValue = DrawerValue.Closed
            )
            val scope = rememberCoroutineScope()

            AppTheme {
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet {
                            NavigationDrawerItem(
                                label = { Text("Local") },
                                selected = false,
                                onClick = {
                                    scope.launch {
                                        drawerState.close()
                                        navController.safeNavigate(Local)
                                    }
                                }
                            )
                            NavigationDrawerItem(
                                label = { Text("Remote") },
                                selected = false,
                                onClick = {
                                    scope.launch {
                                        drawerState.close()
                                        navController.safeNavigate(Remote)
                                    }
                                }
                            )
                        }
                    }
                ) {
                    Scaffold(
                        topBar = {
                            TopBar(
                                navController = navController,
                                onMenuClick = {
                                    scope.launch {
                                        drawerState.open()
                                    }
                                }
                            )
                        },
                        bottomBar = {
                            MusicBar(
                                modifier = Modifier.padding(12.dp),
                                onTrackClick = {
                                    navController.safeNavigate(Track)
                                }
                            )
                        }
                    ) { paddingValues ->

                        val permissionLauncher = rememberLauncherForActivityResult(
                            contract = ActivityResultContracts.RequestPermission()
                        ) { isGranted: Boolean ->
                            if (isGranted) {
                                // Permission is granted
                            } else {
                                // Handle permission denial
                            }
                        }

                        NavHost(
                            navController = navController,
                            startDestination = Local,
                            modifier = Modifier.padding(paddingValues = paddingValues)
                        ) {
                            composable<Local> {
                                HomeScreen()
                                LaunchedEffect(Unit) {
                                    // Check if the permission is already granted
                                    if (ContextCompat.checkSelfPermission(
                                            applicationContext,

                                            Manifest.permission.READ_EXTERNAL_STORAGE
                                        ) != PackageManager.PERMISSION_GRANTED
                                    ) {
                                        // Request the permission
                                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                                    } else {
                                        // Permission already granted
                                        Log.d("Console", "Permission already granted")
                                    }
                                }
                            }
                            composable<Remote> {
                                RemoteTracksScreen()
                            }
                            composable<Track> {
                                TrackScreen()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun NavController.safeNavigate(route: Any) {
        this.currentDestination?.let { destination ->
            if (!destination.hasRoute(route::class)) {
                this.navigate(route) {
                    popUpTo(graph.findStartDestination().id)
                    launchSingleTop = true
                }
            }
        }
    }

    private fun launchPermissionRequest() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.READ_MEDIA_AUDIO
                )
            )
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            )
        }
    }

    fun hasPermissions(permissions: Array<String>): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startService() {
        val serviceIntent = Intent(this, PlayerService::class.java)
        startForegroundService(serviceIntent)
    }
}

@Serializable
object Local

@Serializable
object Remote

@Serializable
object Track