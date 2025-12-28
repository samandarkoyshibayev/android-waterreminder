package com.android.waterreminder.presentation.screens.bodyinfo

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.waterreminder.data.datasource.local.database.UserProfileEntity
import com.android.waterreminder.data.datasource.local.preference.AppPreferenceHelper
import com.android.waterreminder.domain.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BodyInfoScreenViewModel @Inject constructor(
    private val directions: BodyInfoScreenDirections,
    private val appRepository: AppRepository,
    private val appPreferenceHelper: AppPreferenceHelper
) : ViewModel() {
    @RequiresApi(Build.VERSION_CODES.O)
    fun saveBodyInfo(
        gender: String,
        age: Int,
        weight: Int,
        height: Int,
        wakeUpTime: String,
        sleepTime: String
    ) = viewModelScope.launch {
        val finalWakeUpTime = if (wakeUpTime == "Select Time") "07:00 AM" else wakeUpTime
        val finalSleepTime = if (sleepTime == "Select Time") "11:00 PM" else sleepTime

        val info = UserProfileEntity(
            gender = gender,
            age = age,
            weight = weight,
            height = height,
            wakeUpTime = finalWakeUpTime,
            sleepTime = finalSleepTime
        )
        appPreferenceHelper.setOnboardingDone(true)
        appRepository.saveBodyInfo(info)
        directions.navigateToMainScreen()
    }
}