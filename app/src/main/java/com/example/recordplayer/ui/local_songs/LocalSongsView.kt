package com.example.recordplayer.ui.local_songs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recordplayer.R
import com.example.recordplayer.domain.SongModel
import com.example.recordplayer.domain.getPlayListTest
import com.example.recordplayer.ui.player.MiniPlayer
import com.example.recordplayer.ui.player.PlayerEvent
import com.example.recordplayer.ui.player.PlayerState
import com.example.recordplayer.ui.player.PlayerViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LocalSongsView(
    viewModel: LocalSongsViewModel = koinViewModel(),
    playerViewModel: PlayerViewModel = koinViewModel()
) {
    val viewState = viewModel.viewState.collectAsState()
    val playerViewState = playerViewModel.viewState.collectAsState()
    when (val state = viewState.value) {
        is LocalSongsState.Loading -> {
            viewModel.obtainEvent(LocalSongsEvent.LoadData)
            LoadingState()
        }

        is LocalSongsState.Main ->
            MainState(
                state = state,
                playerState = playerViewState.value,
                onPlayButtonClicked = {
                    playerViewModel.obtainEvent(PlayerEvent.PlayPause)
                },
                onSongClick = { songs, currentSongN ->
                    playerViewModel.obtainEvent(PlayerEvent.changePlaylist(songs, currentSongN))
                }
            )
    }
}

@Composable
fun MainState(
    state: LocalSongsState.Main,
    playerState: PlayerState,
    onPlayButtonClicked: () -> Unit = {},
    onSongClick: (songs: List<SongModel>, currentSongN: Int) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LocalSongs(
            state = state,
            onSongClick = onSongClick
        )
        if (playerState is PlayerState.Main)
            MiniPlayer(
                state = playerState,
                onPlayButtonClicked = onPlayButtonClicked
            )
    }
}

@Composable
fun LocalSongs(
    state: LocalSongsState.Main,
    onSongClick: (songs: List<SongModel>, currentSongN: Int) -> Unit
) {
    val searchQuery = remember { mutableStateOf(state.searchQuery) }
    val songs = remember(searchQuery.value) {
        if (searchQuery.value.isEmpty()) {
            state.songs
        } else {
            state.songs.filter {
                it.name.contains(searchQuery.value, ignoreCase = true) ||
                        it.artist.contains(searchQuery.value, ignoreCase = true)
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(vertical = 32.dp, horizontal = 16.dp)
    ) {
        TextField(
            value = searchQuery.value,
            onValueChange = { searchQuery.value = it },
            label = { Text("Поиск") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            maxLines = 1,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search Icon"
                )
            }
        )
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            itemsIndexed(songs) { index, song ->
                SongCard(
                    songs = songs,
                    songN = index,
                    onSongClick = onSongClick
                )
            }
        }
    }
}


@Composable
fun SongCard(
    songs: List<SongModel>,
    songN: Int,
    onSongClick: (songs: List<SongModel>, currentSongN: Int) -> Unit
) {
    val song = songs[songN]
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onSongClick(songs, songN) } // Добавляем кликабельность
    ) {
        Row {
            val placeholderPainter = painterResource(id = R.drawable.record_player)
            val painter: Painter = remember(song.bitmap) {
                song.bitmap?.let {
                    BitmapPainter(it.asImageBitmap())
                } ?: placeholderPainter
            }
            Image(
                painter = painter,
                contentDescription = "Cover",
                modifier = Modifier
                    .size(64.dp)
                    .padding(8.dp)
            )
            Column(
                modifier = Modifier
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = song.name)
                Text(text = song.artist)
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

@Preview(showSystemUi = true)
@Composable
fun PreviewLocalSongsView() {
    MainState(
        state = LocalSongsState.Main(
            songs = getPlayListTest(),
            searchQuery = ""
        ),
        playerState = PlayerState.Loading,
        onPlayButtonClicked = {},
        onSongClick = { _, _ -> }
    )
}
