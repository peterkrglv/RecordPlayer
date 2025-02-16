package com.example.recordplayer.ui.player

import android.util.Log
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
    val viewState: StateFlow<PlayerState>
        get() = _viewState

    init {
        initializePlayer()
        observePlayerState()
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
        val songs = (_viewState.value as? PlayerState.Main)?.songs ?: return
        songs.forEach {
            player.addMediaItem(it.media)
        }
        Log.d("Playyy", "initializePlayer: ${songs.size}")
        player.prepare()
    }

    private fun observePlayerState() {
        viewModelScope.launch {
            while (true) {
                delay(500)
                _viewState.value = (_viewState.value as? PlayerState.Main)?.copy(
                    currentPosition = player.currentPosition,
                    isPlaying = player.isPlaying,
                    sliderPosition = if (player.duration > 0) player.currentPosition.toFloat() / player.duration.toFloat() else 0f,
                    currentSongN = player.currentMediaItemIndex
                ) ?: PlayerState.Loading
            }
        }
    }

    fun obtainEvent(event: PlayerEvent) {
        when (event) {
            is PlayerEvent.LoadData -> loadData()
            is PlayerEvent.PlayPause -> playPause()
            is PlayerEvent.SkipNext -> skipNext()
            is PlayerEvent.SkipPrevious -> skipPrevious()
            is PlayerEvent.SeekTo -> seekTo(event.position)
        }
    }

    private fun seekTo(position: Float) {
        player.seekTo((player.duration * position).toLong())
    }

    private fun skipPrevious() {
        player.seekToPrevious()
        _viewState.value = (_viewState.value as PlayerState.Main).copy(
            currentSongN = player.currentMediaItemIndex
        )
    }

    private fun skipNext() {
        player.seekToNext()
        _viewState.value = (_viewState.value as PlayerState.Main).copy(
            currentSongN = player.currentMediaItemIndex
        )
    }

    private fun playPause() {
        if (_viewState.value !is PlayerState.Main) return
        val isPlaying = (_viewState.value as PlayerState.Main).isPlaying
        if (isPlaying) {
            player.pause()
        } else {
            player.play()
        }
        _viewState.value = (_viewState.value as PlayerState.Main).copy(
            isPlaying = !(isPlaying)
        )
    }

    private fun loadData() {
        // Реализация загрузки данных
    }
}