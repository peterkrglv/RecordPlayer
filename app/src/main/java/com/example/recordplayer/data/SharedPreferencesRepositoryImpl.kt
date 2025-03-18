package com.example.recordplayer.data

import android.content.Context
import android.content.SharedPreferences
import com.example.recordplayer.domain.sharedPrefs.SharedPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SharedPreferencesRepositoryImpl(context: Context): SharedPreferencesRepository {
    private val sharedPrefsFileName = "app_prefs"
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(sharedPrefsFileName, Context.MODE_PRIVATE)
    private val searchHistoryPrefName = "search_history"
    private val separator = ";"

    override suspend fun addSearchHistoryElement(query: String) {
        withContext(Dispatchers.IO) {
            val history = getSearchHistory().toMutableList()
            history.remove(query)
            history.add(0, query)
            if (history.size > 10) {
                history.removeAt(history.size - 1)
            }
            saveSearchHistory(history)
        }
    }

    override suspend fun clearSearchHistory() {
        sharedPreferences.edit().remove("search_history").apply()
    }

    override suspend fun getSearchHistory(): List<String> {
        val historyString = sharedPreferences.getString(searchHistoryPrefName, "") ?: ""
        return if (historyString.isEmpty()) {
            emptyList()
        } else {
            historyString.split(separator).map { it.trim() }
        }
    }

    private fun saveSearchHistory(history: List<String>) {
        val historyString = history.joinToString(separator)
        sharedPreferences.edit().putString(searchHistoryPrefName, historyString).apply()
    }

}