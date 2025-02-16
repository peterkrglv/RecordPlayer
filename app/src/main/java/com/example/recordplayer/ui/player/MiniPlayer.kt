package com.example.recordplayer.ui.player

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.recordplayer.data.LocalSong
import com.example.recordplayer.domain.SongModel

@Composable
fun BoxScope.MiniPlayer(song: SongModel) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .align(Alignment.BottomCenter),
    ) {
        Row(
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
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = song.name)
                Text(text = song.artist)
            }
        }
    }
}