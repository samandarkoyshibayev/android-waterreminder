package com.android.waterreminder.presentation.screens.bodyinfo

import com.android.waterreminder.presentation.screens.main.MainScreen
import com.android.waterreminder.ui.navigation.AppNavigator
import javax.inject.Inject

class BodyInfoScreenDirectionsImpl @Inject constructor(
    private val appNavigator: AppNavigator
) : BodyInfoScreenDirections{
    override suspend fun navigateToMainScreen() {
        appNavigator.replace(MainScreen())
    }
}