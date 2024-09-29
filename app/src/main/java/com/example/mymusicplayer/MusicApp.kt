package com.example.mymusicplayer

import android.app.Application
import android.os.Build
import com.example.mymusicplayer.models.NotificationUtil
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MusicApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationUtil.createChannel(this)
        }
    }
}