package com.example.recordplayer.domain.sharedPrefs

interface SharedPreferencesRepository {
    suspend fun addSearchHistoryElement(query: String)
    suspend fun clearSearchHistory()
    suspend fun getSearchHistory(): List<String>
}