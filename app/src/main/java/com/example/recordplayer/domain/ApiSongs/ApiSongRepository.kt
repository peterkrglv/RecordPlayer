package com.example.recordplayer.domain.ApiSongs

import com.example.recordplayer.data.ApiSong

interface ApiSongRepository {
    fun getExampleSongs(): List<ApiSong>
    fun searchSongs(searchQuery: String): List<ApiSong>
}