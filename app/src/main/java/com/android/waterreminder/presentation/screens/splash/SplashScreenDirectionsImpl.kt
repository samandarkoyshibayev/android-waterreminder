package com.android.waterreminder.presentation.screens.splash

import com.android.waterreminder.presentation.screens.home.HomeScreen
import com.android.waterreminder.presentation.screens.main.MainScreen
import com.android.waterreminder.presentation.screens.main.MainScreenContent
import com.android.waterreminder.presentation.screens.onboarding.OnboardingScreen
import com.android.waterreminder.ui.navigation.AppNavigator
import javax.inject.Inject

class SplashScreenDirectionsImpl @Inject constructor(private val appNavigator: AppNavigator) : SplashScreenDirections {
    override suspend fun navigateToMainScreen() {
        appNavigator.replace(MainScreen())
    }

    override suspend fun navigateToOnboardingScreen() {
        appNavigator.replace(OnboardingScreen())
    }
}