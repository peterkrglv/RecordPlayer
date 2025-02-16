package com.example.recordplayer.data

import android.content.ContentResolver
import android.content.Context
import android.provider.MediaStore
import com.example.recordplayer.domain.LocalSongRepository

class LocalSongRepositoryImpl(private val context: Context) : LocalSongRepository {
    override fun getLocalSongs(): List<LocalSong> {
        val localSongs = mutableListOf<LocalSong>()
        val contentResolver: ContentResolver = context.contentResolver
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM_ID
        )
        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
        val cursor = contentResolver.query(uri, projection, selection, null, null)

        cursor?.use {
            val titleIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val dataIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val albumIdIndex = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
            while (it.moveToNext()) {
                val name = it.getString(titleIndex)
                val artist = it.getString(artistIndex)
                val album = it.getString(albumIndex)
                val path = it.getString(dataIndex)
                val albumId = it.getLong(albumIdIndex)
                val coverPath = getAlbumArt(albumId)
                localSongs.add(LocalSong(name, artist, album, path, coverPath))
            }
        }
        return localSongs
    }

    private fun getAlbumArt(albumId: Long): String? {
        val albumUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Audio.Albums.ALBUM_ART)
        val selection = "${MediaStore.Audio.Albums._ID} = ?"
        val selectionArgs = arrayOf(albumId.toString())
        val cursor =
            context.contentResolver.query(albumUri, projection, selection, selectionArgs, null)
        var albumArt: String? = null
        cursor?.use {
            if (it.moveToFirst()) {
                albumArt = it.getString(it.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_ART))
            }
        }
        return albumArt
    }
}

data class LocalSong(
    val name: String,
    val artist: String,
    val album: String,
    val path: String,
    val coverPath: String?
)
