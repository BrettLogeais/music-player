package com.example.mymusicplayer.di

import com.example.mymusicplayer.models.PlaylistPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun providePlaylistPlayer(): PlaylistPlayer {
        return PlaylistPlayer()
    }
}