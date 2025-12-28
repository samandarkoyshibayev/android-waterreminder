package com.android.waterreminder.data.repository

import android.annotation.SuppressLint
import android.icu.util.LocaleData
import android.os.Build
import androidx.annotation.RequiresApi
import com.android.waterreminder.data.datasource.local.database.DailyWaterRecord
import com.android.waterreminder.data.datasource.local.database.DailyWaterRecordDao
import com.android.waterreminder.data.datasource.local.database.UserInfoDao
import com.android.waterreminder.data.datasource.local.database.UserProfileEntity
import com.android.waterreminder.data.datasource.local.preference.AppPreferenceHelper
import com.android.waterreminder.domain.repository.AppRepository
import com.android.waterreminder.util.WaterCalculator
import com.android.waterreminder.util.WaterReminderScheduler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
class AppRepositoryImpl @Inject constructor(
    private val userInfoDao: UserInfoDao,
    private val dailyWaterRecordDao: DailyWaterRecordDao,
    private val appPreferenceHelper: AppPreferenceHelper,
    private val reminderScheduler: WaterReminderScheduler
) : AppRepository {

    override suspend fun saveBodyInfo(info: UserProfileEntity) {
        userInfoDao.insertUserInfo(info)

        val waterGoal = WaterCalculator.calculateDailyWaterGoal(info)
        appPreferenceHelper.saveTotalWaterGoal(waterGoal.totalWaterMl)

        appPreferenceHelper.setUserSetupComplete(true)

        val interval = appPreferenceHelper.getReminderInterval()
        reminderScheduler.scheduleReminders(interval)
    }

    override suspend fun updateBodyInfo(info: UserProfileEntity) {
        userInfoDao.updateUserInfo(info)

        val waterGoal = WaterCalculator.calculateDailyWaterGoal(info)
        appPreferenceHelper.saveTotalWaterGoal(waterGoal.totalWaterMl)
    }

    override suspend fun getBodyInfo(): Flow<Result<UserProfileEntity?>> = flow {
        val entity = userInfoDao.getLatestUserInfo()
        if (entity != null) {
            emit(Result.success(entity))
        } else {
            emit(Result.failure(Exception("No data found")))
        }
    }.catch { e ->
        emit(Result.failure(e))
    }

    override suspend fun updateReminderInterval(intervalMinutes: Int) {
        appPreferenceHelper.saveReminderInterval(intervalMinutes)

        if (appPreferenceHelper.isReminderEnabled()) {
            reminderScheduler.scheduleReminders(intervalMinutes)
        }
    }

    override suspend fun toggleReminders(enabled: Boolean) {
        appPreferenceHelper.setReminderEnabled(enabled)
        if (enabled) {
            val interval = appPreferenceHelper.getReminderInterval()
            reminderScheduler.scheduleReminders(interval)
        } else {
            reminderScheduler.cancelReminders()
        }
    }

    override fun setLanguage(language: String) {
        appPreferenceHelper.setLanguage(language)
    }

    override fun getLanguage(): String {
        return appPreferenceHelper.getLanguage()
    }

    /* override suspend fun saveDailyRecord(record: DailyWaterRecord) {
         dailyWaterRecordDao.insertRecord(record)
         appPreferenceHelper.saveLastSavedDate(record.date)
     }

     override suspend fun getDailyRecord(date: String): DailyWaterRecord? {
         return dailyWaterRecordDao.getRecordByDate(date)
     }

     override suspend fun getWeeklyRecords(): List<DailyWaterRecord> {
         val today = LocalDate.now()
         val weekAgo = today.minusDays(6)

         val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
         val startDate = weekAgo.format(formatter)
         val endDate = today.format(formatter)

         return dailyWaterRecordDao.getRecordsBetweenDates(startDate, endDate)
     }

     override suspend fun saveCurrentDayProgress() {
         val today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
         val consumed = appPreferenceHelper.getDrunkWaterToday()
         val goal = appPreferenceHelper.getTotalWaterGoal()
         val percentage = if (goal > 0) ((consumed.toFloat() / goal) * 100).toInt() else 0

         val record = DailyWaterRecord(
             date = today,
             waterConsumedMl = consumed,
             goalMl = goal,
             percentageCompleted = percentage.coerceIn(0, 100),
         )
         saveDailyRecord(record)
     }*/
}