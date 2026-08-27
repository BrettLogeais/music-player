package com.example.mymusicplayer.di

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import com.example.mymusicplayer.data.remote.MusicApi
import com.example.mymusicplayer.data.remote.MusicRepository
import com.example.mymusicplayer.domain.MusicRepositoryImpl
import com.example.mymusicplayer.models.ExoPlayerWrapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

const val BASE_URL = "http://blossom:5050/"

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideExoPlayer(
        @ApplicationContext context: Context
    ): ExoPlayerWrapper {
        val player = ExoPlayer.Builder(context)
            .build()
        return ExoPlayerWrapper(player)
    }

    @Provides
    @Singleton
    fun provideMusicApi(): MusicApi {
        val json = Json {
            ignoreUnknownKeys = true
        }
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                json.asConverterFactory("application/json".toMediaType())
            )
            .build()
            .create(MusicApi::class.java)
    }

    @Provides
    fun provideMusicRepository(
        api: MusicApi,
    ): MusicRepository {
        return MusicRepositoryImpl(api)
    }
}