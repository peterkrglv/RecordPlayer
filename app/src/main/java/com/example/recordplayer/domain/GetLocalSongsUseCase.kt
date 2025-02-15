package com.example.recordplayer.domain

class GetLocalSongsUseCase(private val localSongRepository: LocalSongRepository) {
    suspend fun execute() {
        val localSongs = localSongRepository.getLocalSongs()
    }
}