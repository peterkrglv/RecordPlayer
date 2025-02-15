package com.example.recordplayer.domain

class GetLocalSongsUseCase(private val localSongRepository: LocalSongRepository) {
    suspend fun execute() = localSongRepository.getLocalSongs()
}