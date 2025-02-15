package com.example.recordplayer.ui.local_songs

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LocalSongsViewModel : ViewModel() {
    private val _viewState = MutableStateFlow<LocalSongsState>(
        LocalSongsState.Loading
    )
    val viewState: StateFlow<LocalSongsState>
        get() = _viewState
}