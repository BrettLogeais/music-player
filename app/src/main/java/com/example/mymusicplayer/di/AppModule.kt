package com.example.mymusicplayer.di

import android.content.Context
import com.example.mymusicplayer.models.ExoPlayerWrapper
import com.google.android.exoplayer2.ExoPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideExoPlayer(@ApplicationContext context: Context): ExoPlayerWrapper {
        val player = ExoPlayer.Builder(context)
            .build()
        return ExoPlayerWrapper(player)
    }
}