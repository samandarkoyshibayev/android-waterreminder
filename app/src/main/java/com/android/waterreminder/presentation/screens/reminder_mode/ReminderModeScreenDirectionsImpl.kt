package com.android.waterreminder.presentation.screens.reminder_mode

import com.android.waterreminder.ui.navigation.AppNavigator
import javax.inject.Inject

class ReminderModeScreenDirectionsImpl @Inject constructor(
    private val appNavigator: AppNavigator
) : ReminderModeScreenDirections{
    override suspend fun back() {
        appNavigator.back()
    }
}