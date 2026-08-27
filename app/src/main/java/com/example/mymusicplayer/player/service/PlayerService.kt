package com.example.mymusicplayer.player.service

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.MediaMetadata
import android.media.session.MediaSession
import android.media.session.PlaybackState
import android.os.Build
import android.os.IBinder
import android.os.SystemClock
import android.view.KeyEvent
import androidx.annotation.RequiresApi
import androidx.media3.common.MediaItem
import com.example.mymusicplayer.player.PlayerState
import com.example.mymusicplayer.player.notification.NotificationUtil
import com.example.mymusicplayer.player.playback.ExoPlayerWrapper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.system.exitProcess

private const val NOTIFICATION_ID_MEDIA = 1

@AndroidEntryPoint
class PlayerService : Service(), ExoPlayerWrapper.ExoPlayerListener {

    @Inject
    lateinit var player: ExoPlayerWrapper

    private lateinit var mediaSession: MediaSession
    private lateinit var mediaStyle: Notification.MediaStyle
    private lateinit var notificationManager: NotificationManager

    override fun onPlayerStateChanged(playerState: PlayerState) {
        updatePlayback()
        updateNotification()
    }

    override fun onTrackChanged(mediaItem: MediaItem) {
        updateMetadata()
        updateNotification()
    }

    override fun onDurationChanged(duration: Long) {
        updateMetadata()
        updateNotification()
    }

    override fun onPositionChanged(position: Long) {
        updatePlayback()
        updateNotification()
    }

    override fun onCreate() {
        super.onCreate()

        player.addListener(this)

        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        mediaSession = MediaSession(this, "MediaPlayerSessionService")
        mediaStyle =  Notification.MediaStyle()
            .setMediaSession(mediaSession.sessionToken)

        mediaSession.setCallback(object : MediaSession.Callback() {
            @RequiresApi(Build.VERSION_CODES.TIRAMISU)
            override fun onMediaButtonEvent(mediaButtonIntent: Intent): Boolean {
                if (Intent.ACTION_MEDIA_BUTTON == mediaButtonIntent.action) {
                    val event = mediaButtonIntent.getParcelableExtra(
                        Intent.EXTRA_KEY_EVENT,
                        KeyEvent::class.java
                    )
                    event?.let {
                        when (it.keyCode) {
                            KeyEvent.KEYCODE_MEDIA_PLAY -> onPlay()
                            KeyEvent.KEYCODE_MEDIA_PAUSE -> onPause()
                            KeyEvent.KEYCODE_MEDIA_NEXT -> onSkipToNext()
                            KeyEvent.KEYCODE_MEDIA_PREVIOUS -> onSkipToPrevious()
                            else -> {}
                        }
                    }
                }

                return true
            }

            override fun onPlay() {
                player.play()
            }

            override fun onPause() {
                player.pause()
            }

            override fun onSkipToNext() {
                player.next()
                player.play()
            }

            override fun onSkipToPrevious() {
                player.previous()
                player.play()
            }

            override fun onSeekTo(pos: Long) {
                player.seekTo(pos)
            }
        })

        startForeground(NOTIFICATION_ID_MEDIA, NotificationUtil.foregroundNotification(this))
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        player.removeListener(this)
        mediaSession.release()
        stopSelf()
        stopForeground(STOP_FOREGROUND_REMOVE)

        super.onDestroy()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        exitProcess(0)
    }

    private fun updateMetadata() {
        val builder = MediaMetadata.Builder()
        player.currentTrack?.mediaMetadata?.let { metadata ->
            metadata.title?.let {
                builder.putString(MediaMetadata.METADATA_KEY_TITLE, it.toString())
            }
            metadata.artist?.let {
                builder.putString(MediaMetadata.METADATA_KEY_ARTIST, it.toString())
            }
            metadata.albumTitle?.let {
                builder.putString(MediaMetadata.METADATA_KEY_ALBUM, it.toString())
            }
            builder.putLong(MediaMetadata.METADATA_KEY_DURATION, player.getDuration())
        }
        mediaSession.setMetadata(builder.build())
    }

    private fun updatePlayback() {
        val builder = PlaybackState.Builder()
        player.let {
            builder.setState(
                if (it.playerState.isPlaying) PlaybackState.STATE_PLAYING
                else PlaybackState.STATE_PAUSED,
                it.getPosition(),
                1f,
                SystemClock.elapsedRealtime()
            )
            builder.setActions(
                PlaybackState.ACTION_PLAY_PAUSE or
                        PlaybackState.ACTION_SKIP_TO_NEXT or
                        PlaybackState.ACTION_SKIP_TO_PREVIOUS or
                        PlaybackState.ACTION_SEEK_TO
            )
        }
        mediaSession.setPlaybackState(builder.build())
    }

    private fun updateNotification() {
        notificationManager.notify(
            NOTIFICATION_ID_MEDIA,
            NotificationUtil.notificationMediaPlayer(
                this,
                Notification.MediaStyle().setMediaSession(mediaSession.sessionToken)
            )
        )
    }
}