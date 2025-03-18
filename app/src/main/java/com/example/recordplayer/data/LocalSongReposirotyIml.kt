package com.example.recordplayer.data

import android.content.ContentResolver
import android.content.Context
import android.media.MediaMetadataRetriever
import android.provider.MediaStore
import com.example.recordplayer.domain.LocalSongs.LocalSong
import com.example.recordplayer.domain.LocalSongs.LocalSongRepository

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
            while (it.moveToNext()) {
                val name = it.getString(titleIndex)
                val artist = it.getString(artistIndex)
                val album = it.getString(albumIndex)
                val path = it.getString(dataIndex)
                val cover = getSongCover(path)
                localSongs.add(LocalSong(name, artist, album, path, cover))
            }
        }
        return localSongs
    }

    private fun getSongCover(path: String): ByteArray? {
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(path)
            retriever.embeddedPicture
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            retriever.release()
        }
    }
}

