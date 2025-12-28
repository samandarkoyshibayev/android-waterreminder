package com.android.waterreminder.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.android.waterreminder.R
import com.android.waterreminder.activity.MainActivity
import com.android.waterreminder.data.datasource.local.preference.AppPreferenceHelper
import com.android.waterreminder.data.datasource.local.preference.AppPreferenceHelperImpl
import com.android.waterreminder.domain.repository.AppRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import javax.inject.Inject

@HiltWorker
class WaterReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {

    companion object {
        private const val CHANNEL_ID = "water_reminder_channel"
        private const val CHANNEL_NAME = "Water Reminders"
        private const val NOTIFICATION_ID = 1001
    }

    override suspend fun doWork(): Result {
        Log.d("TEST", "doWork")

        createNotificationChannel()
        showReminderNotification()
        return Result.success()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications to remind you to drink water"
                enableVibration(true)
                enableLights(true)
            }

            val notificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showReminderNotification() {
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val messages = listOf(
            "Time to drink water! 💧",
            "Stay hydrated! Your body needs water 💦",
            "Don't forget to drink water! 🥤",
            "Hydration time! Keep your body healthy 💙",
            "Water break! Stay fresh and healthy 🌊",
            "Your body is calling for water! 💧",
            "Stay refreshed! Drink some water now"
        )

        val randomMessage = messages.random()

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_cup)
            .setContentTitle("Water Reminder")
            .setContentText(randomMessage)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setVibrate(longArrayOf(0, 500, 200, 500))
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("$randomMessage\n\nTap to log your water intake")
            )
            .build()

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}