package com.example.recordplayer.ui.api_songs

import com.example.recordplayer.domain.SongModel

sealed class ApiSongsState {
    object Loading : ApiSongsState()
    data class Main(
        val songs: List<SongModel>,
        val searchQuery: String = ""
    ) : ApiSongsState()
}

sealed class ApiSongsEvent {
    object LoadData : ApiSongsEvent()
    data class Search(val query: String) : ApiSongsEvent()
}