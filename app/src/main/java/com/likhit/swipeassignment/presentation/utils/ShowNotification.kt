package com.likhit.swipeassignment.presentation.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.likhit.swipeassignment.MyApp.Companion.CHANNEL_DESC
import com.likhit.swipeassignment.MyApp.Companion.CHANNEL_ID
import com.likhit.swipeassignment.MyApp.Companion.CHANNEL_NAME
import com.likhit.swipeassignment.R
import kotlin.random.Random

fun showNotification(
    context: Context,
    title: String,
    content: String
){
    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.box)
        .setContentTitle(title)
        .setContentText(content)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)
        .build()

    with(NotificationManagerCompat.from(context)){
        if(ActivityCompat.checkSelfPermission(
            context,
            android.Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return@with
        }
        notify(Random.nextInt(), builder)
    }
}