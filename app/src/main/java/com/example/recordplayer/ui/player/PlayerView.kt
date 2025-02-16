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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.recordplayer.R
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
                state = state,
                onPlayButtonClicked = {
                    viewModel.obtainEvent(PlayerEvent.PlayPause)
                },
                onPreviousButtonClicked = {
                    viewModel.obtainEvent(PlayerEvent.SkipPrevious)
                },
                onNextButtonClicked = {
                    viewModel.obtainEvent(PlayerEvent.SkipNext)
                }
            )
        }

        is PlayerState.Loading -> {
            // Отображение состояния загрузки
        }
    }
}

@Composable
fun MainState(
    state: PlayerState.Main,
    onPlayButtonClicked: () -> Unit,
    onPreviousButtonClicked: () -> Unit,
    onNextButtonClicked: () -> Unit,
) {
    val player = state.player
    val songs = state.songs
    val currentPosition = state.currentPosition
    val sliderPosition = state.sliderPosition
    val totalDuration = state.totalDuration
    val isPlaying = state.isPlaying
    val currentSongN = state.currentSongN
    val currentSong = songs[currentSongN]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 32.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = songs[currentSongN].cover),
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
            value = sliderPosition,
            onValueChange = { newPosition ->
                player.seekTo((newPosition * totalDuration).toLong())
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
                text = formatTime(currentPosition)
            )
            Text(
                text = formatTime(totalDuration)
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
                    onPreviousButtonClicked()
                }
            ) {
                Icon(
                    imageVector = SkipPrevious,
                    contentDescription = "Skip Previous"
                )
            }
            IconButton(
                onClick = {
                    onPlayButtonClicked()
                }
            ) {
                Icon(
                    imageVector = if (isPlaying) Pause else Play,
                    contentDescription = "Play/Pause"
                )
            }
            IconButton(
                onClick = onNextButtonClicked
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
    val context = LocalContext.current
    val player = remember { ExoPlayer.Builder(context).build() }
    val songs = listOf(
        SongModel(
            name = "Master Of Puppets",
            artist = "Metallica",
            cover = R.drawable.record_player,
            music = R.raw.never
        )
    )
    val state = PlayerState.Main(
        player = player,
        songs = songs,
        currentSongN = 0,
        isPlaying = false,
        currentPosition = 0,
        sliderPosition = 0f,
        totalDuration = 0
    )
    MainState(state = state,
        onPlayButtonClicked = {},
        onPreviousButtonClicked = {},
        onNextButtonClicked = {}
    )
}

fun formatTime(timeMs: Long): String {
    val totalSeconds = timeMs / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}