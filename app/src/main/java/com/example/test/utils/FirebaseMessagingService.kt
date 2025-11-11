package com.example.test.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import coil3.Bitmap
import coil3.ImageLoader
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import coil3.request.allowHardware
import coil3.request.crossfade
import coil3.toBitmap
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val title = remoteMessage.notification?.title ?: "Message"
        val body = remoteMessage.notification?.body ?: ""
        val imageUrl = remoteMessage.notification?.imageUrl?.toString()

        CoroutineScope(Dispatchers.IO).launch {
            val bitmap = imageUrl?.let { loadBitmap(it) }
            sendNotification(title, body, bitmap)
        }
    }

    private suspend fun loadBitmap(url: String): Bitmap? =
        withContext(Dispatchers.IO) {
            val loader =
                ImageLoader
                    .Builder(this@MyFirebaseMessagingService)
                    .crossfade(true)
                    .build()

            val request =
                ImageRequest
                    .Builder(this@MyFirebaseMessagingService)
                    .data(url)
                    .allowHardware(false)
                    .build()

            val result = loader.execute(request)
            if (result is SuccessResult) {
                result.image.toBitmap()
            } else {
                null
            }
        }

    private fun sendNotification(
        title: String,
        message: String,
        image: Bitmap?,
    ) {
        val channelId = "push_channel"
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel =
            NotificationChannel(
                channelId,
                "Push Notifications",
                NotificationManager.IMPORTANCE_HIGH,
            )
        manager.createNotificationChannel(channel)

        val builder =
            NotificationCompat
                .Builder(this, channelId)
                .setSmallIcon(android.R.drawable.ic_notification_overlay)
                .setContentTitle(title)
                .setContentText(message)

        image?.let {
            builder.setStyle(
                NotificationCompat
                    .BigPictureStyle()
                    .bigPicture(it),
            )
        }

        manager.notify(1, builder.build())
    }
}
