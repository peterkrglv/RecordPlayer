package com.example.recordplayer.domain

class SearchApiSongsUseCase(
    private val apiSongRepository: ApiSongRepository,
    private val searchQuery: String
) {
    suspend fun execute(): List<SongModel> {
        val songs = apiSongRepository.searchSongs(searchQuery)
        return emptyList()
    }
}