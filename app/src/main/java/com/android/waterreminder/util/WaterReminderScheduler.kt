package com.android.waterreminder.util

import android.content.Context
import android.util.Log
import androidx.work.*
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WaterReminderScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val workManager = WorkManager.getInstance(context)

    companion object {
        private const val REMINDER_WORK_TAG = "water_reminder_work"
        private const val REMINDER_UNIQUE_WORK = "water_reminder_unique"
    }

    fun scheduleReminders(intervalMinutes: Int) {
        cancelReminders()

        val validInterval = intervalMinutes.coerceIn(15, 240)
        Log.d("TEST", "$validInterval")
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(false)
            .setRequiresCharging(false)
            .build()

        val reminderWork = PeriodicWorkRequestBuilder<WaterReminderWorker>(
            repeatInterval = validInterval.toLong(),
            repeatIntervalTimeUnit = TimeUnit.MINUTES,
        )
            .setConstraints(constraints)
            .setInitialDelay(validInterval.toLong(), TimeUnit.MINUTES)
            .addTag(REMINDER_WORK_TAG)
            .build()

        workManager.enqueueUniquePeriodicWork(
            REMINDER_UNIQUE_WORK,
            ExistingPeriodicWorkPolicy.REPLACE,
            reminderWork
        )
    }

    fun scheduleImmediateReminder() {
        Log.d("TEST", "scheduleImmediateReminder")
        val workRequest = OneTimeWorkRequestBuilder<WaterReminderWorker>()
            .setInitialDelay(5, TimeUnit.SECONDS)
            .addTag("notification_work")
            .build()
        WorkManager.getInstance(context)
            .enqueueUniqueWork("water_reminder", ExistingWorkPolicy.REPLACE, workRequest)
    }

    fun cancelReminders() {
        workManager.cancelAllWorkByTag(REMINDER_WORK_TAG)
        workManager.cancelUniqueWork(REMINDER_UNIQUE_WORK)
    }
}
