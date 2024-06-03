package com.example.mymusicplayer

import android.app.Application
import com.example.mymusicplayer.di.AppModule
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MusicApp : Application()