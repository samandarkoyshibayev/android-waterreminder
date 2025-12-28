package com.android.waterreminder.presentation.screens.edit

import org.orbitmvi.orbit.ContainerHost

interface EditScreenContract {

    interface ViewModel : ContainerHost<UIState, SideEffect> {
        fun onEventDispatcher(intent: Intent)
    }

    data class UIState(
        val firstName: String = "Unknown",
        val lastName: String = "Unknown",
        val gender: String = "Unknown",
        val age: String  = "",
        val weight: Int = 0,
        val height: Int = 0,
        val wakeUpTime: String = "00:00 AM",
        val sleepTime: String = "00:00 PM"
    )

    sealed interface SideEffect {
        object OnSaved : SideEffect
    }

    sealed interface Intent {
        data class OnFirstNameChange(val value: String) : Intent
        data class OnLastNameChange(val value: String) : Intent
        data class OnAgeChange(val value: String) : Intent
        data class OnHeightChange(val value: String) : Intent
        data class OnWeightChange(val value: String) : Intent
        data class OnGenderChange(val value: String) : Intent
        data class OnWakeUpTimeChange(val value: String) : Intent
        data class OnSleepTimeChange(val value: String) : Intent
        object SaveChanges : Intent
        object Back : Intent
    }
}
