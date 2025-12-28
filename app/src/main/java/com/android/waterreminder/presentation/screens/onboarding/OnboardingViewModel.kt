package com.android.waterreminder.presentation.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.waterreminder.data.datasource.local.preference.AppPreferenceHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val appPreferenceHelper: AppPreferenceHelper,
    private val directions: OnboardingDirections,
) : ViewModel() {

    fun finishOnboarding(name: String, lastname: String) {
        viewModelScope.launch {
            appPreferenceHelper.saveUserName(name)
            appPreferenceHelper.saveUserLastName(lastname)
            directions.navigateToBodyInfoScreen()
        }
    }
}