package com.android.waterreminder.domain.repository

import com.android.waterreminder.data.datasource.local.database.DailyWaterRecord
import com.android.waterreminder.data.datasource.local.database.UserProfileEntity
import kotlinx.coroutines.flow.Flow

interface AppRepository {
    suspend fun saveBodyInfo(info: UserProfileEntity)
    suspend fun getBodyInfo(): Flow<Result<UserProfileEntity?>>
    suspend fun updateBodyInfo(info: UserProfileEntity)

    suspend fun updateReminderInterval(intervalMinutes: Int)
    suspend fun toggleReminders(enabled: Boolean)

    fun setLanguage(language: String)
    fun getLanguage(): String

   /* suspend fun saveDailyRecord(record: DailyWaterRecord)
    suspend fun getDailyRecord(date: String): DailyWaterRecord?
    suspend fun getWeeklyRecords(): List<DailyWaterRecord>
    suspend fun saveCurrentDayProgress()*/
}


