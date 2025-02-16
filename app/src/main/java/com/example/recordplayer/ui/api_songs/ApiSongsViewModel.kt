package com.example.recordplayer.ui.api_songs

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recordplayer.domain.GetApiSongsUseCase
import com.example.recordplayer.domain.SearchApiSongsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Thread.sleep

class ApiSongsViewModel(
    private val getApiSongsUseCase: GetApiSongsUseCase,
    private val searchApiSongsUseCase: SearchApiSongsUseCase
) : ViewModel() {
    private val _viewState = MutableStateFlow<ApiSongsState>(
        ApiSongsState.Loading
    )
    val viewState: StateFlow<ApiSongsState>
        get() = _viewState

    fun obtainEvent(event: ApiSongsEvent) {
        when (event) {
            is ApiSongsEvent.LoadData -> loadData()
            is ApiSongsEvent.Search -> search(event.query)
        }
    }

    private fun search(query: String) {
        Log.d("pizda", "search: $query")
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val songs = searchApiSongsUseCase.execute(query)
                Log.d("pizda", "songs: $songs")
                _viewState.value = ApiSongsState.Main(songs, query)
            }
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