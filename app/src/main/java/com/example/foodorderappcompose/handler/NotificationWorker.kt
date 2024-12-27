package com.example.foodorderappcompose.handler

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.foodorderappcompose.R

class NotificationWorker(context: Context, workerParameters: WorkerParameters): Worker(context, workerParameters) {
    override fun doWork(): Result {
        return try {
            showNotification("Delicious Reminder", "It is time to order if you are hungry!")
            Log.d("NotificationWorker", "Notification succeed")
            Result.success()
        } catch (e: Exception) {
            Log.d("NotificationWorker", "Notification failed")
            Result.failure()
        }
    }

    private fun showNotification(title: String, message: String) {
        val channelId = "dailyNotificationsId"
        val channelName = "Daily Notifications"
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.app_logo)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}