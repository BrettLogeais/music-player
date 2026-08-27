package com.example.mymusicplayer

import android.app.Application
import com.example.mymusicplayer.player.notification.NotificationUtil
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MusicApp : Application() {

    override fun onCreate() {
        super.onCreate()

        NotificationUtil.createChannel(this)
    }
}