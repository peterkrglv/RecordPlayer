package com.example.recordplayer.ui.player

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import coil.compose.rememberImagePainter
import com.example.recordplayer.domain.SongModel
import com.example.recordplayer.ui.icons.Pause
import com.example.recordplayer.ui.icons.Play
import com.example.recordplayer.ui.icons.SkipNext
import com.example.recordplayer.ui.icons.SkipPrevious
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun PlayerView(
    viewModel: PlayerViewModel = koinViewModel()
) {
    val viewState = viewModel.viewState.collectAsState()
    when (val state = viewState.value) {
        is PlayerState.Main -> {
            MainState(
                state = state
            )
        }

        is PlayerState.Loading -> {

        }
    }
}

@Composable
fun MainState(
    state: PlayerState.Main
) {
    val player = state.player
    val songs = remember { mutableStateOf(state.songs) }
    val currentPosition = remember { mutableStateOf(state.currentPosition) }
    val sliderPosition = remember { mutableStateOf(state.sliderPosition) }
    val totalDuration = remember { mutableStateOf(state.totalDuration) }
    val isPlaying = remember { mutableStateOf(state.isPlaying) }
    val currentSongN = remember { mutableStateOf(player.currentMediaItemIndex) }
    val currentSong = songs.value[currentSongN.value]

    LaunchedEffect(Unit) {
        songs.value.forEach {
            player.addMediaItem(it.media)
        }
        player.prepare()
    }

    LaunchedEffect(key1 = player.currentPosition, key2 = player.isPlaying) {
        delay(500)
        currentPosition.value = player.currentPosition
    }

    LaunchedEffect(currentPosition.value) {
        if (totalDuration.value > 0) {
            sliderPosition.value = currentPosition.value.toFloat() / totalDuration.value.toFloat()
        }
    }

    LaunchedEffect(player.duration) {
        if (player.duration > 0) {
            totalDuration.value = player.duration
        }
    }

    LaunchedEffect(isPlaying.value) {
        if (isPlaying.value && totalDuration.value > 0) {
            sliderPosition.value = currentPosition.value.toFloat() / totalDuration.value.toFloat()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 32.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberImagePainter(currentSong.coverPath),
            contentDescription = "Cover",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(32.dp)
        )
        Text(
            text = currentSong.name
        )
        Text(
            text = currentSong.artist
        )
        Slider(
            value = sliderPosition.value,
            onValueChange = { newPosition ->
                sliderPosition.value = newPosition
                player.seekTo((newPosition * totalDuration.value).toLong())
            },
            valueRange = 0f..1f,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = formatTime(currentPosition.value)
            )
            Text(
                text = formatTime(totalDuration.value)
            )
        }
        if (currentSong.album.isNotEmpty()) {
            Text(
                text = currentSong.album
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = {
                    player.seekToPrevious()
                    currentSongN.value = player.currentMediaItemIndex
                }
            ) {
                Icon(
                    imageVector = SkipPrevious,
                    contentDescription = "Skip Previous"
                )
            }
            IconButton(
                onClick = {
                    if (player.isPlaying) {
                        player.pause()
                    } else {
                        player.play()
                    }
                    isPlaying.value = player.isPlaying
                }
            ) {
                Icon(
                    imageVector = if (isPlaying.value) Pause else Play,
                    contentDescription = "Play/Pause"
                )
            }
            IconButton(
                onClick = {
                    player.seekToNext()
                    currentSongN.value = player.currentMediaItemIndex
                }
            ) {
                Icon(
                    imageVector = SkipNext,
                    contentDescription = "Skip Next"
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PlayerPreview() {
}

fun formatTime(timeMs: Long): String {
    val totalSeconds = timeMs / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}