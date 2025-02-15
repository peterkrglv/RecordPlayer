package com.example.recordplayer.ui.player

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.media3.exoplayer.ExoPlayer
import com.example.recordplayer.domain.getPlayList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PlayerViewModel(private val player: ExoPlayer) : ViewModel() {
    private val _viewState = MutableStateFlow<PlayerState>(
        PlayerState.Main(
            player = player,
            songs = getPlayList(),
            currentSongN = 0,
            isPlaying = false,
            currentPosition = 0,
            sliderPosition = 0f,
            totalDuration = 0,
        )
    )
    val viewState: StateFlow<PlayerState>
        get() = _viewState

    fun obtainEvent(event: PlayerEvent) {
        when (event) {
            is PlayerEvent.LoadData -> loadData()
        }
    }

    private fun loadData() {

    }

}