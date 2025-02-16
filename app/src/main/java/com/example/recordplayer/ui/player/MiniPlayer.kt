package com.example.recordplayer.ui.player

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.recordplayer.ui.icons.Pause
import com.example.recordplayer.ui.icons.Play

@Composable
fun BoxScope.MiniPlayer(
    state: PlayerState.Main,
    onPlayButtonClicked: () -> Unit,
) {
    val songs = state.songs
    val currentSongN = state.currentSongN
    if (songs.isEmpty()) return
    val song = songs[currentSongN]
    val isPlaying = state.isPlaying
    Log.d("Playyy", "song: $song")
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .align(Alignment.BottomCenter),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = rememberImagePainter(song.coverPath),
                contentDescription = "Cover",
                modifier = Modifier
                    .size(64.dp)
                    .padding(32.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = song.name,
                    fontWeight = FontWeight.Bold
                )
                Text(text = song.artist)
            }
            IconButton(
                onClick = onPlayButtonClicked,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Icon(
                    imageVector = if (isPlaying) Pause else Play,
                    contentDescription = "Play/Pause"
                )
            }
        }
    }
}