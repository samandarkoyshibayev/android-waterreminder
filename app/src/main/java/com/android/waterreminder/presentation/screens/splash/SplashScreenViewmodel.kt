package com.android.waterreminder.presentation.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.waterreminder.data.datasource.local.preference.AppPreferenceHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewmodel @Inject constructor(
    private val directions: SplashScreenDirections,
    private val appPreferenceHelper: AppPreferenceHelper
) : ViewModel() {
    init {
        viewModelScope.launch {
            delay(2000)
            if (appPreferenceHelper.isOnboardingDone()) {
                directions.navigateToMainScreen()
            } else {
                directions.navigateToOnboardingScreen()
            }
        }
    }
}