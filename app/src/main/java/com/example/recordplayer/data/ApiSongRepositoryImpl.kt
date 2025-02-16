package com.example.recordplayer.data

import com.example.recordplayer.domain.ApiSongRepository
import org.json.JSONObject
import java.net.URL

class ApiSongRepositoryImpl : ApiSongRepository {
    override fun getExampleSongs(): List<ApiSong> {
        val url = URL("https://api.deezer.com/chart")
        val response = url.readText()
        val json = JSONObject(response)
        val tracks = json.getJSONObject("tracks").getJSONArray("data")
        val songs = mutableListOf<ApiSong>()

        for (i in 0 until tracks.length()) {
            val track = tracks.getJSONObject(i)
            val song = ApiSong(
                name = track.getString("title"),
                artist = track.getJSONObject("artist").getString("name"),
                album = track.getJSONObject("album").getString("title"),
                media = URL(track.getString("preview")),
                cover = URL(track.getJSONObject("album").getString("cover"))
            )
            songs.add(song)
        }
        return songs
    }

    override fun searchSongs(searchQuery: String): List<ApiSong> {
        val url = URL("https://api.deezer.com/search?q=$searchQuery")
        val response = url.readText()
        val json = JSONObject(response)
        val songs = mutableListOf<ApiSong>()

        if (json.has("data")) {
            val tracks = json.getJSONArray("data")
            for (i in 0 until tracks.length()) {
                val track = tracks.getJSONObject(i)
                val song = ApiSong(
                    name = track.getString("title"),
                    artist = track.getJSONObject("artist").getString("name"),
                    album = track.getJSONObject("album").getString("title"),
                    media = URL(track.getString("preview")),
                    cover = URL(track.getJSONObject("album").getString("cover"))
                )
                songs.add(song)
            }
        }
        return songs
    }
}

data class ApiSong(
    val name: String,
    val artist: String,
    val album: String,
    val media: URL,
    val cover: URL
)