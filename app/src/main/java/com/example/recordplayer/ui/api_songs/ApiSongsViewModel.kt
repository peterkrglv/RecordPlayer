package com.example.recordplayer.ui.api_songs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recordplayer.domain.GetApiSongsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Thread.sleep

class ApiSongsViewModel(
    private val getApiSongsUseCase: GetApiSongsUseCase
) : ViewModel() {
    private val _viewState = MutableStateFlow<ApiSongsState>(
        ApiSongsState.Loading
    )
    val viewState: StateFlow<ApiSongsState>
        get() = _viewState

    fun obtainEvent(event: ApiSongsEvent) {
        when (event) {
            is ApiSongsEvent.LoadData -> loadData()
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                sleep(1000)
                val songs = getApiSongsUseCase.execute()
                _viewState.value = ApiSongsState.Main(songs)
            }
        }
    }
}