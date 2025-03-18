package com.example.recordplayer.domain.LocalSongs

interface LocalSongRepository {
    fun getLocalSongs(): List<LocalSong>
}

data class LocalSong(
    val name: String,
    val artist: String,
    val album: String,
    val path: String,
    val cover: ByteArray?
)