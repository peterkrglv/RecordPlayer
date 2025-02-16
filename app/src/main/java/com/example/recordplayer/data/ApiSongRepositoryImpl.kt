package com.example.recordplayer.data

import com.example.recordplayer.domain.ApiSongRepository

class ApiSongRepositoryImpl: ApiSongRepository {
    override fun getExampleSongs(): List<ApiSong> {
        TODO("Not yet implemented")
    }

    override fun searchSongs(searchQuery: String): List<ApiSong> {
        TODO("Not yet implemented")
    }

}

data class ApiSong(
    val name: String,
    val artist: String,
    val album: String,
) {

}
