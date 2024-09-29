package com.example.mymusicplayer.service

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.MediaMetadata
import android.os.Build
import android.os.IBinder
import android.os.SystemClock
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.view.KeyEvent
import androidx.annotation.RequiresApi
import androidx.media.app.NotificationCompat
import com.example.mymusicplayer.models.ExoPlayerWrapper
import com.example.mymusicplayer.models.NotificationUtil
import com.example.mymusicplayer.models.PlayerState
import com.google.android.exoplayer2.MediaItem
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.system.exitProcess

private const val NOTIFICATION_ID_MEDIA = 1

@AndroidEntryPoint
class PlayerService : Service(), ExoPlayerWrapper.ExoPlayerListener {

    @Inject
    lateinit var player: ExoPlayerWrapper

    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaStyle: NotificationCompat.MediaStyle
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
        mediaSession = MediaSessionCompat(this, "MediaPlayerSessionService")
        mediaStyle =  NotificationCompat.MediaStyle()
            .setMediaSession(mediaSession.sessionToken)

        mediaSession.setCallback(object : MediaSessionCompat.Callback() {
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
        val builder = MediaMetadataCompat.Builder()
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
        val builder = PlaybackStateCompat.Builder()
        player.let {
            builder.setState(
                if (it.playerState.isPlaying) PlaybackStateCompat.STATE_PLAYING
                else PlaybackStateCompat.STATE_PAUSED,
                it.getPosition(),
                1f,
                SystemClock.elapsedRealtime()
            )
            builder.setActions(
                PlaybackStateCompat.ACTION_PLAY_PAUSE or
                        PlaybackStateCompat.ACTION_SKIP_TO_NEXT or
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or
                        PlaybackStateCompat.ACTION_SEEK_TO
            )
        }
        mediaSession.setPlaybackState(builder.build())
    }

    private fun updateNotification() {
        notificationManager.notify(
            NOTIFICATION_ID_MEDIA,
            NotificationUtil.notificationMediaPlayer(
                this,
                NotificationCompat.MediaStyle().setMediaSession(mediaSession.sessionToken)
            )
        )
    }
}