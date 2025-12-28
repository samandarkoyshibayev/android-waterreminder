package com.android.waterreminder.presentation.screens.analysis

import com.android.waterreminder.data.datasource.local.database.UserProfileEntity
import com.android.waterreminder.presentation.screens.edit.EditScreenContract.Intent
import org.orbitmvi.orbit.ContainerHost

interface AnalysisScreenContract {

    interface VieModel : ContainerHost<UIState, SideEffect> {
        fun onEventDispatcher(intent: Intent)
    }

    data class DayData(
        val day: String,
        val percentage: Int,
        val waterMl: Int,
        val isToday: Boolean = false
    )

    data class UIState(
        val targetWater: Int = 0,
        val totalDrunkWater: Int = 0,
        val bmi: Float = 0F,
        val calories: String = "0",
        val steps: String = "0",
        val heartRate: Int = 0,
        val weeklyData: List<DayData> = emptyList(),
        val averageCompletion: Int = 0,
        val isLoading: Boolean = false
    )

    interface SideEffect

    interface Intent {
        object LoadData : Intent
        object RefreshWeeklyData : Intent
    }
}