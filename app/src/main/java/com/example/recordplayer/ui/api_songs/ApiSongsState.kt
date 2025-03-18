package com.example.recordplayer.ui.api_songs

import com.example.recordplayer.domain.SongModel

sealed class ApiSongsState {
    data object Loading : ApiSongsState()
    data class Main(
        val songs: List<SongModel>,
        val exampleSongs: List<SongModel>,
        val searchQuery: String = "",
        val searchHistory: List<String> = emptyList(),
        val isLoading: Boolean = false
    ) : ApiSongsState()
}

sealed class ApiSongsEvent {
    data object LoadData : ApiSongsEvent()
    data class Search(val query: String) : ApiSongsEvent()
    data class QueryChanged(val query: String) : ApiSongsEvent()
    data object ClearSearchHistory : ApiSongsEvent()
}