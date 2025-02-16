package com.example.recordplayer.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.recordplayer.R
import java.net.URL
import kotlin.concurrent.thread

class NotificationService : Service() {

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("Notiffff", "onStartCommand")
        val title = intent?.getStringExtra("title") ?: "Default Title"
        val artist = intent?.getStringExtra("artist") ?: "Default Artist"
        val coverUrl = intent?.getStringExtra("coverUrl")
        val bitmapByteArray = intent?.getByteArrayExtra("bitmap")
        val bitmap = bitmapByteArray?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }

        if (bitmap != null) {
            sendNotification(title, artist, bitmap)
        } else if (coverUrl != null) {
            thread {
                val coverBitmap = getBitmapFromURL(coverUrl)
                sendNotification(title, artist, coverBitmap)
            }
        } else {
            sendNotification(title, artist, null)
        }

        return START_NOT_STICKY
    }

    private fun sendNotification(title: String, artist: String, coverBitmap: Bitmap?) {
        Log.d("Notiffff", "NotificationManager created")
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "my_channel_id"
            val channel = NotificationChannel(
                channelId,
                "My Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
            Log.d("Notiffff", "Channel created")
        }

        val notificationBuilder = NotificationCompat.Builder(this, "my_channel_id")
            .setContentTitle(title)
            .setContentText(artist)
            .setSmallIcon(R.drawable.record_player)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        if (coverBitmap != null) {
            notificationBuilder.setLargeIcon(coverBitmap)
        } else {
            notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.record_player))
        }

        val notification = notificationBuilder.build()
        notificationManager.notify(1, notification)
    }

    private fun getBitmapFromURL(src: String): Bitmap? {
        return try {
            val url = URL(src)
            BitmapFactory.decodeStream(url.openConnection().getInputStream())
        } catch (e: Exception) {
            Log.e("Notiffff", "Error fetching image", e)
            null
        }
    }
}