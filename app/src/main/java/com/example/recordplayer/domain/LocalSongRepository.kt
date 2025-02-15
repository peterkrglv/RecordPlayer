package com.example.recordplayer.domain

import com.example.recordplayer.data.LocalSong

interface LocalSongRepository {
    fun getLocalSongs(): List<LocalSong>
}