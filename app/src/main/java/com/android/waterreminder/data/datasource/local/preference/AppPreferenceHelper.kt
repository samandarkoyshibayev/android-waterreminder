package com.android.waterreminder.data.datasource.local.preference

import kotlinx.coroutines.flow.Flow

interface AppPreferenceHelper {

    fun setOnboardingDone(value: Boolean)
    fun isOnboardingDone(): Boolean

    fun saveUserName(name: String)
    fun getUserName(): String?

    fun saveUserLastName(name: String)
    fun getUserLastName(): String?

    fun getDrunkWaterToday(): Int
    fun saveDrunkWaterToday(amount: Int)

    fun getTotalWaterGoal(): Int
    fun saveTotalWaterGoal(goal: Int)

    fun getLastDrinkAmount(): Int
    fun saveLastDrinkAmount(amount: Int)

    fun getLastDrinkTime(): String
    fun saveLastDrinkTime(time: String)

    fun resetDailyData()

    fun getReminderInterval(): Int
    fun saveReminderInterval(interval: Int)

    fun isReminderEnabled(): Boolean
    fun setReminderEnabled(enabled: Boolean)

    fun isFirstLaunch(): Boolean
    fun setFirstLaunchComplete()
    fun isUserSetupComplete(): Boolean
    fun setUserSetupComplete(complete: Boolean)

    fun clearAllData()

    fun getThemeMode(): Flow<String>
    fun saveThemeMode(mode: String)

    fun getLastSavedDate(): String
    fun saveLastSavedDate(date: String)

    fun setLanguage(language: String)
    fun getLanguage(): String
}