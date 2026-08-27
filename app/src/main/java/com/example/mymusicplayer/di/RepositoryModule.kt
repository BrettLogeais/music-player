package com.example.mymusicplayer.di

import com.example.mymusicplayer.feature.playlist.data.repository.DeviceMusicRepositoryImpl
import com.example.mymusicplayer.feature.playlist.domain.MusicRepository
import com.example.mymusicplayer.feature.playlist.data.repository.MusicRepositoryImpl
import com.example.mymusicplayer.feature.playlist.domain.DeviceMusicRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindDeviceMusicRepository(
        impl: DeviceMusicRepositoryImpl,
    ): DeviceMusicRepository

    @Binds
    @Singleton
    abstract fun bindMusicRepository(
        impl: MusicRepositoryImpl,
    ): MusicRepository
}