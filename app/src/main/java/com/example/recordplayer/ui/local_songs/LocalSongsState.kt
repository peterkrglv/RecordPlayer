package com.example.recordplayer.ui.local_songs

import com.example.recordplayer.domain.SongModel

sealed class LocalSongsState {
    object Loading : LocalSongsState()
    data class Main(
        val songs: List<SongModel>,
        val searchQuery: String = ""
    ) : LocalSongsState()
}

sealed class LocalSongsEvent {
    object LoadData : LocalSongsEvent()
}