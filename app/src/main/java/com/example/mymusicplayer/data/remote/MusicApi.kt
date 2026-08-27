package com.example.mymusicplayer.data.remote

import com.example.mymusicplayer.data.remote.dto.TrackDto
import retrofit2.http.GET

interface MusicApi {
    @GET("tracks")
    suspend fun getTracks(): List<TrackDto>
}