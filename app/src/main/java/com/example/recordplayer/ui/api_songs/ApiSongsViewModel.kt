package com.example.recordplayer.ui.api_songs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recordplayer.domain.ApiSongs.GetApiSongsUseCase
import com.example.recordplayer.domain.ApiSongs.SearchApiSongsUseCase
import com.example.recordplayer.domain.sharedPrefs.ClearSearchHistoryUseCase
import com.example.recordplayer.domain.sharedPrefs.GetSearchHistoryUseCase
import com.example.recordplayer.domain.sharedPrefs.SaveSearchQueryUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ApiSongsViewModel(
    private val getApiSongsUseCase: GetApiSongsUseCase,
    private val searchApiSongsUseCase: SearchApiSongsUseCase,
    private val clearSearchHistoryUseCase: ClearSearchHistoryUseCase,
    private val saveSearchQueryUseCase: SaveSearchQueryUseCase,
    private val getSearchHistoryUseCase: GetSearchHistoryUseCase
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
            is ApiSongsEvent.QueryChanged -> queryChanged(event.query)
            is ApiSongsEvent.ClearSearchHistory -> clearSearchHistory()
        }
    }

    private fun queryChanged(query: String) {
        val state = _viewState.value
        if (state !is ApiSongsState.Main) {
            return
        }
        if (query == "") {
            _viewState.value = state.copy(searchQuery = query, songs = state.exampleSongs)
        } else {
            _viewState.value = state.copy(searchQuery = query)
        }
    }

    private fun clearSearchHistory() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                clearSearchHistoryUseCase.execute()
                val state = _viewState.value
                if (state is ApiSongsState.Main) {
                    _viewState.value = state.copy(searchHistory = emptyList(), songs = state.exampleSongs)
                }
            }
        }
    }

    private fun search(query: String) {
        val state = _viewState.value
        if (state !is ApiSongsState.Main) {
            return
        }
        if (query == "") {
            _viewState.value = state.copy(songs = state.exampleSongs, searchQuery = query, searchHistory = state.searchHistory)
        }
        _viewState.value = state.copy(isLoading = true)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val songs = searchApiSongsUseCase.execute(query)
                val searchHistory = saveSearchQueryUseCase.execute(query)
                _viewState.value = state.copy(songs = songs, searchQuery = query, searchHistory = searchHistory)
            }
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val songs = getApiSongsUseCase.execute()
                val searchHistory = getSearchHistoryUseCase.execute()
                _viewState.value = ApiSongsState.Main(exampleSongs = songs, songs = songs, searchHistory = searchHistory)
            }
        }
    }
}