package com.example.mymusicplayer.di

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import com.example.mymusicplayer.models.ExoPlayerWrapper
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