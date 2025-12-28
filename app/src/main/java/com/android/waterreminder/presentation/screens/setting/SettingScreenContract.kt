package com.android.waterreminder.presentation.screens.setting

import com.android.waterreminder.util.LanguageEnum
import org.orbitmvi.orbit.ContainerHost

interface SettingScreenContract {
    interface ViewModel : ContainerHost<UIState, SideEffect> {
        fun onEventDispatcher(intent: Intent)
    }

    data class UIState(
        val reminderInterval: Int = 60,
        val intakeGoal: Int = 2000,
        val isReminderEnabled: Boolean = true,
        val themeMode: String = "System",
        val showThemeDialog: Boolean = false,
        val language: LanguageEnum = LanguageEnum.EN,
        val showLanguageDialog: Boolean = false
    )

    sealed class Intent {
        object LoadSettings : Intent()
        object OpenReminderMode : Intent()
        data class OpenComingSoonMessage(val message: String) : Intent()
        data class ToggleReminders(val enabled: Boolean) : Intent()
        object OpenThemeDialog : Intent()
        object CloseThemeDialog : Intent()
        data class SelectTheme(val theme: String) : Intent()
        object OpenLanguageDialog : Intent()
        object CloseLanguageDialog : Intent()
        data class SelectLanguage(val language: LanguageEnum) : Intent()
    }

    sealed interface SideEffect {
        data class ShowMessage(val message: String) : SideEffect
        object RecrateActivity : SideEffect
    }
}