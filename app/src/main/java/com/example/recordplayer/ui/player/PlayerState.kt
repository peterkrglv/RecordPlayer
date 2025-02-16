package com.example.recordplayer.ui.player

import androidx.media3.exoplayer.ExoPlayer
import com.example.recordplayer.domain.SongModel

sealed class PlayerState {
    object Loading : PlayerState()
    data class Main(
        val player: ExoPlayer,
        val songs: List<SongModel>,
        val isPlaying: Boolean,
        val currentSongN: Int,
        val currentPosition: Long,
        val sliderPosition: Float,
        val totalDuration: Long
    ) : PlayerState()
}

sealed class PlayerEvent {
    data object LoadData : PlayerEvent()
    data object PlayPause: PlayerEvent()
    data object SkipNext: PlayerEvent()
    data object SkipPrevious: PlayerEvent()
    data class SeekTo(val position: Float): PlayerEvent()
    data class changePlaylist(val songs: List<SongModel>, val currentSongN: Int): PlayerEvent()
}