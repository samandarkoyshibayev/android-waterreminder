package com.android.waterreminder.data.datasource.local.preference

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPreferenceHelperImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AppPreferenceHelper {

    private val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_ONBOARDING_DONE = "onboarding_done"
        private const val KEY_NAME = "key_name"
        private const val KEY_LASTNAME = "key_lastname"
        private const val KEY_DRUNK_WATER_TODAY = "drunk_water_today"
        private const val KEY_TOTAL_WATER_GOAL = "total_water_goal"
        private const val KEY_LAST_DRINK_AMOUNT = "last_drink_amount"
        private const val KEY_LAST_DRINK_TIME = "last_drink_time"
        private const val KEY_LAST_UPDATE_DATE = "last_update_date"

        private const val KEY_REMINDER_INTERVAL = "reminder_interval"
        private const val KEY_REMINDER_ENABLED = "reminder_enabled"

        private const val KEY_IS_FIRST_LAUNCH = "is_first_launch"
        private const val KEY_USER_SETUP_COMPLETE = "user_setup_complete"

        private const val DEFAULT_WATER_GOAL = 2000
        private const val DEFAULT_REMINDER_INTERVAL = 60

        private const val KEY_LAST_SAVED_DATE = "last_saved_date"

        private const val KEY_THEME_MODE = "theme_mode"
        const val THEME_SYSTEM = "System"
        private const val KEY_LANGUAGE = "language"
    }

    override fun setOnboardingDone(value: Boolean) {
        prefs.edit { putBoolean(KEY_ONBOARDING_DONE, value) }
    }

    override fun isOnboardingDone(): Boolean {
        return prefs.getBoolean(KEY_ONBOARDING_DONE, false)
    }

    override fun saveUserName(name: String) {
        prefs.edit { putString(KEY_NAME, name) }
    }

    override fun getUserName(): String? = prefs.getString(KEY_NAME, "")

    override fun saveUserLastName(name: String) {
        prefs.edit { putString(KEY_LASTNAME, name) }
    }

    override fun getUserLastName(): String? = prefs.getString(KEY_LASTNAME, "")

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkAndResetIfNewDay() {
        val today = java.time.LocalDate.now().toString()
        val lastDate = prefs.getString(KEY_LAST_UPDATE_DATE, "")

        if (lastDate != today) {
            prefs.edit {
                putInt(KEY_DRUNK_WATER_TODAY, 0)
                    .putInt(KEY_LAST_DRINK_AMOUNT, 0)
                    .putString(KEY_LAST_DRINK_TIME, "")
                    .putString(KEY_LAST_UPDATE_DATE, today)
            }
        }
    }

    override fun getDrunkWaterToday(): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) checkAndResetIfNewDay()
        return prefs.getInt(KEY_DRUNK_WATER_TODAY, 0)
    }

    override fun saveDrunkWaterToday(amount: Int) {
        val today = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            java.time.LocalDate.now().toString() else ""
        prefs.edit {
            putInt(KEY_DRUNK_WATER_TODAY, amount)
                .putString(KEY_LAST_UPDATE_DATE, today)
        }
    }

    override fun getTotalWaterGoal(): Int {
        return prefs.getInt(KEY_TOTAL_WATER_GOAL, 2000) // Default 2000ml
    }

    override fun saveTotalWaterGoal(goal: Int) {
        prefs.edit { putInt(KEY_TOTAL_WATER_GOAL, goal) }
    }

    override fun getLastDrinkAmount(): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) checkAndResetIfNewDay()
        return prefs.getInt(KEY_LAST_DRINK_AMOUNT, 0)
    }

    override fun saveLastDrinkAmount(amount: Int) {
        prefs.edit { putInt(KEY_LAST_DRINK_AMOUNT, amount) }
    }

    override fun getLastDrinkTime(): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) checkAndResetIfNewDay()
        return prefs.getString(KEY_LAST_DRINK_TIME, "") ?: ""
    }

    override fun saveLastDrinkTime(time: String) {
        prefs.edit { putString(KEY_LAST_DRINK_TIME, time) }
    }

    override fun resetDailyData() {
        prefs.edit {
            putInt(KEY_DRUNK_WATER_TODAY, 0)
            putInt(KEY_LAST_DRINK_AMOUNT, 0)
            putString(KEY_LAST_DRINK_TIME, "")
        }
    }

    override fun getReminderInterval(): Int {
        return prefs.getInt(KEY_REMINDER_INTERVAL, DEFAULT_REMINDER_INTERVAL)
    }

    override fun saveReminderInterval(interval: Int) {
        prefs.edit {
            putInt(KEY_REMINDER_INTERVAL, interval.coerceIn(15, 240))
        }
    }

    override fun isReminderEnabled(): Boolean {
        return prefs.getBoolean(KEY_REMINDER_ENABLED, false)
    }

    override fun setReminderEnabled(enabled: Boolean) {
        prefs.edit {
            putBoolean(KEY_REMINDER_ENABLED, enabled)
        }
    }

    override fun isFirstLaunch(): Boolean {
        return prefs.getBoolean(KEY_IS_FIRST_LAUNCH, true)
    }

    override fun setFirstLaunchComplete() {
        prefs.edit {
            putBoolean(KEY_IS_FIRST_LAUNCH, false)
        }
    }

    override fun isUserSetupComplete(): Boolean {
        return prefs.getBoolean(KEY_USER_SETUP_COMPLETE, false)
    }

    override fun setUserSetupComplete(complete: Boolean) {
        prefs.edit {
            putBoolean(KEY_USER_SETUP_COMPLETE, complete)
        }
    }

    override fun clearAllData() {
        prefs.edit { clear() }
    }

    override fun getThemeMode(): Flow<String> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
            if (key == KEY_THEME_MODE) {
                trySend(prefs.getString(KEY_THEME_MODE, THEME_SYSTEM) ?: THEME_SYSTEM)
            }
        }
        prefs.registerOnSharedPreferenceChangeListener(listener)
        trySend(prefs.getString(KEY_THEME_MODE, THEME_SYSTEM) ?: THEME_SYSTEM)
        awaitClose { prefs.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    override fun saveThemeMode(mode: String) {
        prefs.edit { putString(KEY_THEME_MODE, mode) }
    }

    override fun getLastSavedDate(): String {
        return prefs.getString(KEY_LAST_SAVED_DATE, "") ?: ""
    }

    override fun saveLastSavedDate(date: String) {
        prefs.edit { putString(KEY_LAST_SAVED_DATE, date) }
    }

    override fun setLanguage(language: String) {
        prefs.edit { putString(KEY_LANGUAGE, language) }
    }

    override fun getLanguage(): String {
        return prefs.getString(KEY_LANGUAGE, "en") ?: "en"
    }
}
