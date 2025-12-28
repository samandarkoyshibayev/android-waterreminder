package com.android.waterreminder.presentation.screens.reminder_mode

import org.orbitmvi.orbit.ContainerHost

interface ReminderModeContract {
    interface ViewModel : ContainerHost<UIState, SideEffect> {
        fun onEventDispatcher(intent: Intent)
    }

    data class UIState(
        val selectedInterval: Int = 60,
        val currentInterval: Int = 60
    )

    sealed class Intent {
        object LoadCurrentInterval : Intent()
        data class SelectInterval(val interval: Int) : Intent()
        object SaveInterval : Intent()
        object NavigateBack : Intent()
    }

    sealed interface SideEffect {
        data class ShowMessage(val message: String) : SideEffect
    }
}