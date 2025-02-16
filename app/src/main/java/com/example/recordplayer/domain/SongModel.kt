package com.example.recordplayer.domain

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.media3.common.MediaItem
import com.example.recordplayer.data.LocalSong

data class SongModel(
    val name: String,
    val artist: String,
    val album: String,
    val media: MediaItem,
    val cover: ByteArray?,
    val bitmap: Bitmap?
) {
    constructor(localSong: LocalSong): this(
        name = localSong.name,
        artist = localSong.artist,
        album = localSong.album,
        media = MediaItem.Builder()
            .setUri(localSong.path)
            .setMediaId(localSong.path)
            .build(),
        cover = localSong.cover,
        bitmap = localSong.cover?.let {
            BitmapFactory.decodeByteArray(it, 0, it.size)
        }
    )
}

fun getPlayListTest(): List<SongModel> {
    val localSongs = listOf(
        LocalSong(
            name = "Never Gonna Give You Up",
            artist = "Rick Astley",
            album = "Whenever You Need Somebody",
            path = "android.resource://com.example.recordplayer/raw/never",
            cover = null
        ),
        LocalSong(
            name = "Мозги & деньги",
            artist = "Комсомольск",
            album = "Комсомольск-1",
            path = "android.resource://com.example.recordplayer/raw/money_and_brains",
            cover = null
        ),
        LocalSong(
            name = "Lose Yourself",
            artist = "Eminem",
            album = "8 Mile",
            path = "android.resource://com.example.recordplayer/raw/never",
            cover = null
        ),
        LocalSong(
            name = "Crazy",
            artist = "Gnarls Barkley",
            album = "St. Elsewhere",
            path = "android.resource://com.example.recordplayer/raw/never",
            cover = null
        ),
        LocalSong(
            name = "Till I Collapse",
            artist = "Eminem",
            album = "The Eminem Show",
            path = "android.resource://com.example.recordplayer/raw/never",
            cover = null
        ),
    )
    return localSongs.map { SongModel(it) }
}