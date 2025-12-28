package com.android.waterreminder.presentation.screens.profile

import org.orbitmvi.orbit.ContainerHost

interface ProfileScreenContract {

    interface ViewModel : ContainerHost<UIState, SideEffect> {
        fun onEventDispatcher(intent: Intent)
    }

    data class UIState(
        val height: Int = 0,
        val weight: Int = 0,
        val wakeUptime: String = "--'--",
        val sleepTime: String = "--'--",
        val firstname: String = "",
        val lastname: String = "",
        val age: Int = 0,
        val gender: String = "Male",
    )

    sealed interface SideEffect

    sealed interface Intent {
        object OnEditClicked : Intent
    }
}