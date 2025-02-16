package com.example.recordplayer.ui.player

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.recordplayer.R
import com.example.recordplayer.domain.SongModel

class Notifications(context: Context) {
//    private val notificationId = 1
//    private val channelId = "miniplayer_channel"
//
//    init {
//        showMiniPlayerNotification(
//            context = context,
//            song = (_viewState.value as PlayerState.Main).songs[0],
//            isPlaying = false
//        )
//    }
//
//    private fun showMiniPlayerNotification(
//        context: Context,
//        song: SongModel,
//        isPlaying: Boolean
//    ) {
//        val notificationManager = NotificationManagerCompat.from(context)
//        val notification = NotificationCompat.Builder(context, channelId)
//            .setSmallIcon(R.drawable.player)
//            .setContentTitle(song.name)
//            .setContentText(song.artist)
//            .setPriority(NotificationCompat.PRIORITY_LOW)
//            .setOnlyAlertOnce(true)
//            .setOngoing(true)
//            .addAction(
//                R.drawable.skip_previous,
//                "Previous",
//                getPendingIntent(context, "PREV")
//            )
//            .addAction(
//                if (isPlaying) R.drawable.pause else R.drawable.play,
//                if (isPlaying) "Pause" else "Play",
//                getPendingIntent(context, if (isPlaying) "PAUSE" else "PLAY")
//            )
//            .addAction(
//                R.drawable.skip_next,
//                "Next",
//                getPendingIntent(context, "NEXT")
//            )
//            .build()
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                channelId,
//                "MiniPlayer",
//                NotificationManager.IMPORTANCE_LOW
//            )
//            notificationManager.createNotificationChannel(channel)
//        }
//
//        notificationManager.notify(notificationId, notification)
//    }
//
//    private fun getPendingIntent(context: Context, action: String): PendingIntent {
//        val intent = Intent(context, NotificationActionReceiver::class.java).apply {
//            this.action = action
//        }
//        return PendingIntent.getBroadcast(
//            context,
//            0,
//            intent,
//            PendingIntent.FLAG_UPDATE_CURRENT
//        )
//    }
}