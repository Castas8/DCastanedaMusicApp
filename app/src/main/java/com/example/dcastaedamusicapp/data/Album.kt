package com.example.dcastaedamusicapp.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Album(
    @SerializedName("id")
    val id: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("artist")
    val artist: String,

    @SerializedName("cover_url")
    val coverUrl: String,

    @SerializedName("description")
    val description: String?
) : Serializable