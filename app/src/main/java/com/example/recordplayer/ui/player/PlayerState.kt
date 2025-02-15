package com.example.recordplayer.ui.player

import androidx.media3.exoplayer.ExoPlayer
import com.example.recordplayer.domain.SongModel

sealed class PlayerState {
    object Loading : PlayerState()
    data class Main(
        val player: ExoPlayer,
        val songs: List<SongModel>,
        val currentSongN: Int,
        val isPlaying: Boolean,
        val currentPosition: Long,
        val sliderPosition: Float,
        val totalDuration: Long
    ) : PlayerState()
}

sealed class PlayerEvent {
    data object LoadData : PlayerEvent()
}