package com.example.recordplayer.ui.local_songs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recordplayer.domain.LocalSongs.GetLocalSongsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Thread.sleep

class LocalSongsViewModel(
    private val getLocalSongsUseCase: GetLocalSongsUseCase
) : ViewModel() {
    private val _viewState = MutableStateFlow<LocalSongsState>(
        LocalSongsState.Loading
    )
    val viewState: StateFlow<LocalSongsState>
        get() = _viewState

    fun obtainEvent(event: LocalSongsEvent) {
        when (event) {
            is LocalSongsEvent.LoadData -> loadData()
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                sleep(1000)
                val songs = getLocalSongsUseCase.execute()
                _viewState.value = LocalSongsState.Main(songs)
            }
        }
    }
}