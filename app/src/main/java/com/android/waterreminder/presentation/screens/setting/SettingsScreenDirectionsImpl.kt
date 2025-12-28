package com.android.waterreminder.presentation.screens.setting

import com.android.waterreminder.presentation.screens.edit.EditScreen
import com.android.waterreminder.presentation.screens.reminder_mode.ReminderModeScreen
import com.android.waterreminder.ui.navigation.AppNavigator
import javax.inject.Inject

class SettingsScreenDirectionsImpl @Inject constructor(
    val appNavigator: AppNavigator
): SettingsScreenDirections{

    override suspend fun navigateToReminderModeScreen() {
        appNavigator.push(ReminderModeScreen())
    }
}