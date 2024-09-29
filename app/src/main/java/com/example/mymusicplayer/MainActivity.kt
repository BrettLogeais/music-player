package com.example.mymusicplayer

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.compose.AppTheme
import com.example.mymusicplayer.models.ExoPlayerWrapper
import com.example.mymusicplayer.service.PlayerService
import com.example.mymusicplayer.ui.MusicBar
import com.example.mymusicplayer.ui.screens.HomeScreen
import com.example.mymusicplayer.ui.screens.TrackScreen
import com.example.mymusicplayer.viewmodels.TopBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity(), ServiceConnection {

    @Inject
    lateinit var player: ExoPlayerWrapper

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.entries.forEach {
            Log.d("DEBUG", "${it.key} = ${it.value}")
        }
    }

    private var mediaPlayerService: PlayerService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launchPermissionRequest()

        // Bind service
        bindService(
            startService(),
            this,
            BIND_AUTO_CREATE
        )

        setContent {
            val navController = rememberNavController()

            AppTheme {
                Scaffold(
                    topBar = { TopBar() },
                    bottomBar = {
                        MusicBar(
                            modifier = Modifier.padding(12.dp)
                        ) { navController.safeNavigate(Track) }
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
                        startDestination = Home,
                        modifier = Modifier.padding(paddingValues = paddingValues)
                    ) {
                        composable<Home> {
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
                        composable<Track> {
                            TrackScreen()
                        }
                    }
                }
            }
        }
    }

    private fun NavController.safeNavigate(route: Any) {
        this.currentDestination?.let { destination ->
            if (!destination.hasRoute(route::class))
                this.navigate(route)
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

    private fun startService(): Intent {
        val serviceIntent = Intent(this, PlayerService::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else startService(serviceIntent)

        return serviceIntent
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            unbindService(this)
        } catch (_: IllegalArgumentException) { }
    }

    override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
        val binder = p1 as PlayerService.PlayerServiceBinder
        mediaPlayerService = binder.getService()
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
        mediaPlayerService = null
    }
}

@Serializable
object Home

@Serializable
object Track