package com.example.recordplayer.ui.player

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.recordplayer.domain.getPlayList
import com.example.recordplayer.ui.icons.Pause
import com.example.recordplayer.ui.icons.Play
import com.example.recordplayer.ui.icons.SkipNext
import com.example.recordplayer.ui.icons.SkipPrevious
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
    val player = remember { mutableStateOf(state.player) }
    val songs = remember { mutableStateOf(state.songs) }
    val currentPosition = remember { mutableStateOf(state.currentPosition) }
    val sliderPosition = remember { mutableStateOf(state.sliderPosition) }
    val totalDuration = remember { mutableStateOf(state.totalDuration) }
    val isPlaying = remember { mutableStateOf(state.isPlaying) }
    val currentSongN = remember { mutableStateOf(player.value.currentMediaItemIndex) }

    Log.d("playy", "pos: ${player.value.currentPosition}")
    Log.d("playy", "isPlaying: ${player.value.isPlaying}")
    Column(
        modifier = Modifier.fillMaxSize(),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (currentSongN.value > -1) songs.value[currentSongN.value].name else ""
        )
        Row(
            modifier = Modifier
        ) {
            IconButton(
                modifier = Modifier.padding(32.dp),
                onClick = {
                    player.value.seekToPrevious()
                    currentSongN.value = player.value.currentMediaItemIndex
                }
            ) {
                Icon(
                    imageVector = SkipPrevious,
                    contentDescription = "Skip Previous"
                )
            }
            IconButton(
                modifier = Modifier.padding(32.dp),
                onClick = {
                    if (player.value.isPlaying) {
                        player.value.pause()
                    } else {
                        player.value.play()
                    }
                    isPlaying.value = player.value.isPlaying
                }
            ) {
                Icon(
                    imageVector = if (isPlaying.value) Pause else Play,
                    contentDescription = "Play/Pause"
                )
            }
            IconButton(
                modifier = Modifier.padding(32.dp),
                onClick = {
                    player.value.seekToNext()
                    currentSongN.value = player.value.currentMediaItemIndex
                }
            ) {
                Icon(
                    imageVector = SkipNext,
                    contentDescription = "Skip Next"
                )
            }
        }
    }

    val packageName = "com.example.recordplayer"
    LaunchedEffect(Unit) {
        songs.value.forEach {
            val path = "android.resource://" + packageName + "/" + it.music
            val mediaItem = MediaItem.fromUri(Uri.parse(path))
            player.value.addMediaItem(mediaItem)
        }
        player.value.prepare()
    }
}

@Preview(showSystemUi = true)
@Composable
fun PlayerPreview() {
    val player = ExoPlayer.Builder(LocalContext.current).build()
    val songs = getPlayList()
    MainState(
        state = PlayerState.Main(
            player = player,
            songs = songs,
            currentSongN = 0,
            isPlaying = false,
            currentPosition = 0,
            sliderPosition = 0f,
            totalDuration = 0
        )
    )
}