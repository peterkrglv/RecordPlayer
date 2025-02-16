package com.example.recordplayer.domain

class GetApiSongsUseCase(private val apiSongRepository: ApiSongRepository) {
    suspend fun execute(): List<SongModel> {
        val songs = apiSongRepository.getExampleSongs()
        return emptyList()
    }
}