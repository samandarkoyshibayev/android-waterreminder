package com.android.waterreminder.presentation.screens.onboarding

import com.android.waterreminder.presentation.screens.bodyinfo.BodyInfoScreen
import com.android.waterreminder.ui.navigation.AppNavigator
import javax.inject.Inject

class OnboardingDirectionsImpl @Inject constructor(
    private val appNavigator: AppNavigator
) : OnboardingDirections {
    override suspend fun navigateToBodyInfoScreen() {
        appNavigator.replace(BodyInfoScreen())
    }
}