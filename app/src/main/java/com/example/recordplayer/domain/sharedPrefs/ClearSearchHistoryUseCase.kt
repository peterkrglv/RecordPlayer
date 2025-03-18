package com.example.recordplayer.domain.sharedPrefs

class ClearSearchHistoryUseCase(private val repo: SharedPreferencesRepository) {
    suspend fun execute() = repo.clearSearchHistory()
}