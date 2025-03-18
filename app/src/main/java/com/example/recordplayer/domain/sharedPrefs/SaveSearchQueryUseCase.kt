package com.example.recordplayer.domain.sharedPrefs

class SaveSearchQueryUseCase(private val repo: SharedPreferencesRepository) {
    suspend fun execute(query: String): List<String> {
        repo.addSearchHistoryElement(query)
        return repo.getSearchHistory()
    }
}