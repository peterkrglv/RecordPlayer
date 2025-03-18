package com.example.recordplayer.domain.ApiSongs

import com.example.recordplayer.domain.SongModel

class GetApiSongsUseCase(private val apiSongRepository: ApiSongRepository) {
    suspend fun execute(): List<SongModel> {
        val songs = apiSongRepository.getExampleSongs()
        return songs.map{ SongModel(it) }
    }
}