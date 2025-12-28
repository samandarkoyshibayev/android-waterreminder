package com.android.waterreminder.presentation.screens.splash


interface SplashScreenDirections {
    suspend fun navigateToMainScreen()
    suspend fun navigateToOnboardingScreen()
}