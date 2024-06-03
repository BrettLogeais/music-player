package com.example.mymusicplayer.di

import android.content.Context
import com.example.mymusicplayer.LoadingState
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
    fun provideLoadingState(): LoadingState {
        return LoadingState()
    }
}