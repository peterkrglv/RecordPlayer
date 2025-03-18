package com.example.recordplayer.ui.api_songs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.recordplayer.domain.SongModel
import com.example.recordplayer.ui.player.MiniPlayer
import com.example.recordplayer.ui.player.PlayerEvent
import com.example.recordplayer.ui.player.PlayerState
import com.example.recordplayer.ui.player.PlayerViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ApiSongsView(
    viewModel: ApiSongsViewModel = koinViewModel(),
    playerViewModel: PlayerViewModel = koinViewModel()
) {
    val viewState = viewModel.viewState.collectAsState()
    val playerViewState = playerViewModel.viewState.collectAsState()
    when (val state = viewState.value) {
        is ApiSongsState.Loading -> {
            viewModel.obtainEvent(ApiSongsEvent.LoadData)
            LoadingState()
        }

        is ApiSongsState.Main ->
            MainState(
                state = state,
                playerState = playerViewState.value,
                onPlayButtonClicked = {
                    playerViewModel.obtainEvent(PlayerEvent.PlayPause)
                },
                onSongClick = { songs, currentSongN ->
                    playerViewModel.obtainEvent(PlayerEvent.ChangePlaylist(songs, currentSongN))
                },
                onSearch = { query ->
                    viewModel.obtainEvent(ApiSongsEvent.Search(query))
                },
                onQueryChanged = { query ->
                    viewModel.obtainEvent(ApiSongsEvent.QueryChanged(query))
                }
            )
    }
}

@Composable
fun MainState(
    state: ApiSongsState.Main,
    playerState: PlayerState,
    onPlayButtonClicked: () -> Unit = {},
    onSongClick: (songs: List<SongModel>, currentSongN: Int) -> Unit,
    onQueryChanged: (String) -> Unit,
    onSearch: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(vertical = 32.dp, horizontal = 16.dp)
        ) {
            DropDownSearchBar(
                searchQuery = state.searchQuery,
                onSearch = { query -> onSearch(query) },
                queryHistory = state.searchHistory,
                onQueryChanged = onQueryChanged
            )
            if (state.isLoading) {
                LoadingState()
            } else {
                ApiSongs(
                    state = state,
                    onSongClick = onSongClick
                )
            }
        }
        if (playerState is PlayerState.Main)
            MiniPlayer(
                state = playerState,
                onPlayButtonClicked = onPlayButtonClicked
            )
    }
}

@Composable
fun ApiSongs(
    state: ApiSongsState.Main,
    onSongClick: (songs: List<SongModel>, currentSongN: Int) -> Unit
) {
    val songs = state.songs
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
        item {
            if (songs.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Ничего не найдено"
                    )

                }
            }
        }
        item {
            Spacer(modifier = Modifier.size(64.dp))
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
    val imagePainter = rememberImagePainter(data = song.coverUrl)
    val painter: Painter = song.bitmap?.let {
        BitmapPainter(it.asImageBitmap())
    } ?: imagePainter

    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onSongClick(songs, songN) }
    ) {
        Row {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownSearchBar(
    searchQuery: String,
    onSearch: (String) -> Unit,
    queryHistory: List<String>,
    onQueryChanged: (String) -> Unit
) {
    val expanded = remember { mutableStateOf(false) }
    val history = queryHistory
        .filter { it.contains(searchQuery, ignoreCase = true) && it != searchQuery }
        .take(6)
    ExposedDropdownMenuBox(
        modifier = Modifier.fillMaxWidth(),
        expanded = expanded.value,
        onExpandedChange = { expanded.value = !expanded.value }
    ) {
        TextField(
            value = searchQuery,
            onValueChange = {
                onQueryChanged(it)
                expanded.value = true
            },
            label = { Text("Поиск в интернете") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .menuAnchor(),
            maxLines = 1,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search Icon",
                    modifier = Modifier.clickable {
                        onSearch(searchQuery)
                        expanded.value = false
                    }
                )
            },
            trailingIcon = {
                if (searchQuery != "") {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "Search Icon",
                        modifier = Modifier.clickable {
                            onQueryChanged("")
                        }
                    )
                }
            }
        )
        if (history.isNotEmpty()) {
            ExposedDropdownMenu(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest),
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false },
            ) {
                history.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item) },
                        onClick = {
                            onSearch(item)
                            expanded.value = false
                        }
                    )
                }
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
fun PreviewApiSongsView() {
    MainState(
        state = ApiSongsState.Main(
            songs = listOf(),
            searchQuery = "",
            exampleSongs = emptyList()
        ),
        playerState = PlayerState.Loading,
        onPlayButtonClicked = {},
        onSongClick = { _, _ -> },
        onQueryChanged = { _ -> },
        onSearch = { _ -> }
    )
}