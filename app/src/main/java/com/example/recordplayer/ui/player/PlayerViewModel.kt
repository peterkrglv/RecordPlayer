package com.example.recordplayer.ui.player

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.recordplayer.domain.SongModel
import com.example.recordplayer.domain.LocalSongs.GetLocalSongsUseCase
import com.example.recordplayer.ui.NotificationService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import android.graphics.Bitmap

class PlayerViewModel(
    private val player: ExoPlayer,
    application: Application,
    private val getLocalSongsUseCase: GetLocalSongsUseCase
) : AndroidViewModel(application) {
    private val _viewState = MutableStateFlow<PlayerState>(
        PlayerState.Loading
    )
    val viewState: StateFlow<PlayerState>
        get() = _viewState

    init {
        loadData()
        observePlayerState()
    }

    private fun initializePlayer(songs: List<SongModel>) {
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
                val currentState = _viewState.value as? PlayerState.Main ?: return@launch
                _viewState.value = currentState.copy(
                    currentPosition = player.currentPosition,
                    isPlaying = player.isPlaying,
                    sliderPosition = if (player.duration > 0) player.currentPosition.toFloat() / player.duration.toFloat() else 0f,
                    currentSongN = player.currentMediaItemIndex
                )
                sendNotification(currentState.songs[currentState.currentSongN])
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
            is PlayerEvent.ChangePlaylist -> changePlaylist(event.songs, event.currentSongN)
        }
    }

    private fun changePlaylist(songs: List<SongModel>, currentSongN: Int) {
        viewModelScope.launch {
            player.clearMediaItems()
            songs.forEach {
                player.addMediaItem(it.media)
            }
            player.seekTo(currentSongN, 0)
            player.prepare()
            player.play()
            _viewState.value = PlayerState.Main(
                player = player,
                songs = songs,
                isPlaying = true,
                currentSongN = currentSongN,
                currentPosition = 0L,
                sliderPosition = 0f,
                totalDuration = 0L
            )
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
        viewModelScope.launch {
            val songs = getLocalSongsUseCase.execute()
            _viewState.value = PlayerState.Main(
                player = player,
                songs = songs,
                currentSongN = 0,
                isPlaying = false,
                currentPosition = 0,
                sliderPosition = 0f,
                totalDuration = 0
            )
            initializePlayer(songs)
            sendNotification(songs[0])
        }
    }

    fun sendNotification(song: SongModel) {
        val intent = Intent(getApplication(), NotificationService::class.java).apply {
            putExtra("title", song.name)
            putExtra("artist", song.artist)
            putExtra("coverUrl", song.coverUrl)
            song.bitmap?.let {
                val stream = ByteArrayOutputStream()
                it.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val byteArray = stream.toByteArray()
                putExtra("bitmap", byteArray)
            }
        }
        getApplication<Application>().startService(intent)
    }
}