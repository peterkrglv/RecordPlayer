
package com.example.recordplayer.data

import com.example.recordplayer.domain.ApiSongs.ApiSongRepository
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.net.URL

class ApiSongRepImpl : ApiSongRepository {
    private val api: DeezerApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.deezer.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(DeezerApi::class.java)
    }

    override fun getExampleSongs(): List<ApiSong> {
        val response = api.getExampleSongs().execute()
        return if (response.isSuccessful) {
            val songs = response.body()?.tracks?.data ?: emptyList()
            songs.map { it.toApiSong() }
        } else {
            emptyList()
        }
    }

    override fun searchSongs(searchQuery: String): List<ApiSong> {
        val response = api.searchSongs(searchQuery).execute()
        return if (response.isSuccessful) {
            val songs = response.body()?.data ?: emptyList()
            songs.map { it.toApiSong() }
        } else {
            emptyList()
        }
    }
}

interface DeezerApi {
    @GET("chart")
    fun getExampleSongs(): Call<DeezerResponse>

    @GET("search")
    fun searchSongs(@Query("q") searchQuery: String): Call<SearchResponse>
}

data class DeezerResponse(
    @SerializedName("tracks") val tracks: Tracks
)

data class Tracks(
    @SerializedName("data") val data: List<ApiSongRaw>
)

data class SearchResponse(
    @SerializedName("data") val data: List<ApiSongRaw>
)

data class ApiSongRaw(
    @SerializedName("title") val name: String,
    @SerializedName("artist") val artist: Artist,
    @SerializedName("album") val album: Album,
    @SerializedName("preview") val media: URL,
    @SerializedName("cover") val cover: String?
) {
    fun toApiSong(): ApiSong {
        return ApiSong(
            name = this.name,
            artist = this.artist.name,
            album = this.album.title,
            media = this.media,
            cover = URL(this.cover ?: this.album.cover)
        )
    }
}

data class Artist(
    @SerializedName("name") val name: String
)

data class Album(
    @SerializedName("title") val title: String,
    @SerializedName("cover") val cover: String
)

//data class ApiSong(
//    val name: String,
//    val artist: String,
//    val album: String,
//    val media: URL,
//    val cover: URL
//)