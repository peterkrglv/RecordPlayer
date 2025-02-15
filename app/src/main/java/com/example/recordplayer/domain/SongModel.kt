package com.example.recordplayer.domain

import com.example.recordplayer.R

data class SongModel(
    val name: String,
    val artist: String,
    val music: Int,
    val cover: Int
)

fun getPlayList(): List<SongModel> {
    return listOf(
        SongModel(
            name = "Master Of Puppets",
            artist = "Metallica",
            cover = R.drawable.record_player,
            music = R.raw.never
        ),
        SongModel(
            name = "Everyday Normal Guy 2",
            artist = "Jon Lajoie",
            cover = R.drawable.record_player,
            music = R.raw.money_and_brains
        ),
        SongModel(
            name = "Lose Yourself",
            artist = "Eminem",
            cover = R.drawable.record_player,
            music = R.raw.never
        ),
        SongModel(
            name = "Crazy",
            artist = "Gnarls Barkley",
            cover = R.drawable.record_player,
            music = R.raw.never
        ),
        SongModel(
            name = "Till I Collapse",
            artist = "Eminem",
            cover = R.drawable.record_player,
            music = R.raw.money_and_brains
        ),
    )
}