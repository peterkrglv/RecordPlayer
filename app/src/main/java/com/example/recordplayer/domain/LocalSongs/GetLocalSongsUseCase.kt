package com.example.recordplayer.domain.LocalSongs

import com.example.recordplayer.domain.SongModel

class GetLocalSongsUseCase(private val localSongRepository: LocalSongRepository) {
    suspend fun execute(): List<SongModel> {
        val localSongs = localSongRepository.getLocalSongs()
        return localSongs.map { SongModel(it) }
    }
}