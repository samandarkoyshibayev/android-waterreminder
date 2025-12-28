package com.android.waterreminder.presentation.screens.home

import org.orbitmvi.orbit.ContainerHost
import java.time.LocalTime

interface HomeScreenContract {

    interface ViewModel : ContainerHost<UIState, SideEffect> {
        fun onEventDispatcher(intent: Intent)
    }

    data class UIState(
        val fullName: String = "",
        val totalWaterMl: Int = 0,
        val drunkWaterMl: Int = 0,
        val progressPercent: Int = 0,
        val lastDrinkAmount: Int = 0,
        val lastDrinkTime: LocalTime? = null,
        val isGoalReached: Boolean = false,
        val canAddDrink: Boolean = true
    )

    sealed class Intent {
        object LoadData : Intent()
        data class AddDrink(val amount: Int) : Intent()
        object ShowReminder : Intent()
    }

    sealed interface SideEffect {
        data class ShowMessage(val message: String) : SideEffect
    }
}