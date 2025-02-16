package com.example.recordplayer.domain

class SearchApiSongsUseCase(
    private val apiSongRepository: ApiSongRepository,
) {
    suspend fun execute(searchQuery: String): List<SongModel> {
        val songs = apiSongRepository.searchSongs(searchQuery)
        return songs.map{ SongModel(it) }
    }
}