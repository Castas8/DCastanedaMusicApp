package com.example.dcastaedamusicapp.data


import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("api/albums")
    suspend fun getAlbums(): List<Album>

    @GET("api/albums/{id}")
    suspend fun getAlbumDetail(@Path("id") albumId: String): Album
}