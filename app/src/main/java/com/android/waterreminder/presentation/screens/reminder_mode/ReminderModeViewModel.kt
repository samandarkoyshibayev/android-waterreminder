package com.android.waterreminder.presentation.screens.reminder_mode

import androidx.lifecycle.ViewModel
import com.android.waterreminder.data.datasource.local.preference.AppPreferenceHelper
import com.android.waterreminder.util.WaterReminderScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class ReminderModeViewModel @Inject constructor(
    private val appPreferenceHelper: AppPreferenceHelper,
    private val directions: ReminderModeScreenDirections,
    private val reminderScheduler: WaterReminderScheduler
) : ReminderModeContract.ViewModel, ViewModel() {

    override val container =
        container<ReminderModeContract.UIState, ReminderModeContract.SideEffect>(
            ReminderModeContract.UIState()
        )

    override fun onEventDispatcher(intent: ReminderModeContract.Intent) {
        when (intent) {
            ReminderModeContract.Intent.LoadCurrentInterval -> loadCurrentInterval()
            is ReminderModeContract.Intent.SelectInterval -> selectInterval(intent.interval)
            ReminderModeContract.Intent.SaveInterval -> saveInterval()
            ReminderModeContract.Intent.NavigateBack -> navigateBack()
        }
    }

    private fun loadCurrentInterval() = intent {
        val interval = appPreferenceHelper.getReminderInterval()
        reduce {
            state.copy(
                selectedInterval = interval,
                currentInterval = interval
            )
        }
    }

    private fun selectInterval(interval: Int) = intent {
        reduce {
            state.copy(selectedInterval = interval)
        }
    }

    private fun saveInterval() = intent {
        appPreferenceHelper.saveReminderInterval(state.selectedInterval)

        if (appPreferenceHelper.isReminderEnabled()) {
            reminderScheduler.scheduleReminders(state.selectedInterval)
        }
        postSideEffect(
            ReminderModeContract.SideEffect.ShowMessage(
                "Reminder interval updated to ${state.selectedInterval} minutes"
            )
        )
        navigateBack()
    }

    private fun navigateBack() = intent {
        directions.back()
    }
}