package com.example.recordplayer.domain

import android.util.Log

class GetLocalSongsUseCase(private val localSongRepository: LocalSongRepository) {
    suspend fun execute(): List<SongModel> {
        val localSongs = localSongRepository.getLocalSongs()
        return localSongs.map{ SongModel(it)}
    }
}