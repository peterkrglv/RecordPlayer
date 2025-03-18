package com.example.recordplayer.ui.player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.recordplayer.ui.api_songs.LoadingState
import com.example.recordplayer.ui.icons.Pause
import com.example.recordplayer.ui.icons.Play
import com.example.recordplayer.ui.icons.RecordPlayer
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
                state = state,
                onPlayButtonClicked = {
                    viewModel.obtainEvent(PlayerEvent.PlayPause)
                },
                onPreviousButtonClicked = {
                    viewModel.obtainEvent(PlayerEvent.SkipPrevious)
                },
                onNextButtonClicked = {
                    viewModel.obtainEvent(PlayerEvent.SkipNext)
                },
                onSliderPositionChanged = { newPosition ->
                    viewModel.obtainEvent(PlayerEvent.SeekTo(newPosition))
                },
            )
        }

        is PlayerState.Loading -> {
            LoadingState()
        }
    }
}

@Composable
fun MainState(
    state: PlayerState.Main,
    onPlayButtonClicked: () -> Unit,
    onPreviousButtonClicked: () -> Unit,
    onNextButtonClicked: () -> Unit,
    onSliderPositionChanged: (Float) -> Unit
) {
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
        val placeholderPainter = rememberVectorPainter(image = RecordPlayer)
        val painter: Painter = remember(currentSong.bitmap) {
            currentSong.bitmap?.let {
                BitmapPainter(it.asImageBitmap())
            } ?: placeholderPainter
        }
        Icon(
            painter = painter,
            contentDescription = "Cover",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(32.dp)
        )
//        Image(
//            painter = painter,
//            contentDescription = "Cover",
//            modifier = Modifier
//                .fillMaxWidth()
//                .aspectRatio(1f)
//                .padding(32.dp)
//        )
        Text(
            text = currentSong.name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = currentSong.artist
        )
        if (currentSong.album.isNotEmpty()) {
            Text(
                text = currentSong.album
            )
        }
        Slider(
            value = sliderPosition,
            onValueChange = { newPosition ->
                onSliderPositionChanged(newPosition)
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

@Composable
fun LoadingState() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.background
            ),
    ) {
        CircularProgressIndicator()
    }
}


fun formatTime(timeMs: Long): String {
    val totalSeconds = timeMs / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}

@Preview(showSystemUi = true)
@Composable
fun PlayerPreview() {

}