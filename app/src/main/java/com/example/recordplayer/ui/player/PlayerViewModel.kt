package com.example.recordplayer.ui.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.recordplayer.domain.getPlayListTest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val player: ExoPlayer
) : ViewModel() {

    private val _viewState = MutableStateFlow<PlayerState>(
        PlayerState.Main(
            player = player,
            songs = getPlayListTest(),
            currentSongN = 0,
            isPlaying = false,
            currentPosition = 0,
            sliderPosition = 0f,
            totalDuration = 0
        )
    )
    val viewState: StateFlow<PlayerState> = _viewState

    init {
        initializePlayer()
    }

    private fun initializePlayer() {
        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    _viewState.value = (_viewState.value as? PlayerState.Main)?.copy(
                        totalDuration = player.duration
                    ) ?: PlayerState.Loading
                }
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                _viewState.value = (_viewState.value as? PlayerState.Main)?.copy(
                    currentSongN = player.currentMediaItemIndex
                ) ?: PlayerState.Loading
            }
        })
    }

    fun updateCurrentPosition() {
        viewModelScope.launch {
            while (true) {
                if (player.isPlaying) {
                    _viewState.value = (_viewState.value as? PlayerState.Main)?.copy(
                        currentPosition = player.currentPosition
                    ) ?: PlayerState.Loading
                }
                delay(1000)
            }
        }
    }
}