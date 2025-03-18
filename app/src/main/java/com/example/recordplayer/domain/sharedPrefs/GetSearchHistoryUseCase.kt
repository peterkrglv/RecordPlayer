package com.example.recordplayer.domain.sharedPrefs

class GetSearchHistoryUseCase(private val repo: SharedPreferencesRepository) {
    suspend fun execute(): List<String> = repo.getSearchHistory()
}