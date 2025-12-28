package com.android.waterreminder.presentation.screens.setting

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.android.waterreminder.data.datasource.local.preference.AppPreferenceHelper
import com.android.waterreminder.domain.repository.AppRepository
import com.android.waterreminder.util.LanguageEnum
import com.android.waterreminder.util.LocaleHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject
import kotlin.code

@HiltViewModel
class SettingScreenViewModel @Inject constructor(
    private val appRepository: AppRepository,
    private val appPreferenceHelper: AppPreferenceHelper,
    private val directions: SettingsScreenDirections,
    @ApplicationContext private val context: Context
) : SettingScreenContract.ViewModel, ViewModel() {

    override val container =
        container<SettingScreenContract.UIState, SettingScreenContract.SideEffect>(
            SettingScreenContract.UIState()
        )

    override fun onEventDispatcher(intent: SettingScreenContract.Intent) {
        when (intent) {
            SettingScreenContract.Intent.LoadSettings -> loadSettings()
            SettingScreenContract.Intent.OpenReminderMode -> openReminderMode()
            is SettingScreenContract.Intent.OpenComingSoonMessage -> comingSoonMessage(message = intent.message)
            is SettingScreenContract.Intent.ToggleReminders -> toggleReminders(intent.enabled)
            SettingScreenContract.Intent.CloseThemeDialog -> closeThemeDialog()
            SettingScreenContract.Intent.OpenThemeDialog -> openThemeDialog()
            is SettingScreenContract.Intent.SelectTheme -> selectTheme(theme = intent.theme)
            SettingScreenContract.Intent.CloseLanguageDialog -> closeLanguageDialog()
            SettingScreenContract.Intent.OpenLanguageDialog -> openLanguageDialog()
            is SettingScreenContract.Intent.SelectLanguage -> selectLanguage(language = intent.language)
        }
    }

    private fun loadSettings() = intent {
        val interval = appPreferenceHelper.getReminderInterval()
        val goal = appPreferenceHelper.getTotalWaterGoal()
        val isEnabled = appPreferenceHelper.isReminderEnabled()

        val language = appRepository.getLanguage()
        intent {
            val newLang = when (language) {
                "uz" -> LanguageEnum.UZ
                "ru" -> LanguageEnum.RU
                "en" -> LanguageEnum.EN
                else -> LanguageEnum.EN
            }
            reduce {
                state.copy(
                    language = newLang
                )
            }
        }
        val theme = appPreferenceHelper.getThemeMode().first()
        reduce {
            state.copy(
                reminderInterval = interval,
                intakeGoal = goal,
                isReminderEnabled = isEnabled,
                themeMode = theme
            )
        }
    }

    private fun openThemeDialog() = intent {
        reduce {
            state.copy(showThemeDialog = true)
        }
    }

    private fun closeThemeDialog() = intent {
        reduce {
            state.copy(showThemeDialog = false)
        }
    }

    private fun selectTheme(theme: String) = intent {
        intent {
            appPreferenceHelper.saveThemeMode(theme)

            reduce {
                state.copy(
                    themeMode = theme,
                    showThemeDialog = false
                )
            }

            postSideEffect(
                SettingScreenContract.SideEffect.ShowMessage(
                    "Theme changed to ${theme.replaceFirstChar { it.uppercase() }}"
                )
            )
        }
    }

    private fun openLanguageDialog() = intent {
        Log.d("LANGUAGE", "CLICKED")
        reduce {
            state.copy(showLanguageDialog = true)
        }
    }

    private fun closeLanguageDialog() = intent {
        reduce {
            state.copy(showLanguageDialog = false)
        }
    }

    private fun selectLanguage(language: LanguageEnum) = intent {
        when (language) {
            LanguageEnum.EN -> {
                appRepository.setLanguage("en")
                LocaleHelper.setLocale(context, LanguageEnum.EN.code)
                postSideEffect(SettingScreenContract.SideEffect.RecrateActivity)
            }

            LanguageEnum.RU -> {
                appRepository.setLanguage("ru")
                LocaleHelper.setLocale(context, LanguageEnum.RU.code)
                postSideEffect(SettingScreenContract.SideEffect.RecrateActivity)
            }

            LanguageEnum.UZ -> {
                appRepository.setLanguage("uz")
                LocaleHelper.setLocale(context, LanguageEnum.UZ.code)
                postSideEffect(SettingScreenContract.SideEffect.RecrateActivity)
            }
        }
        reduce {
            state.copy(
                language = language,
                showLanguageDialog = false
            )
        }

        postSideEffect(
            SettingScreenContract.SideEffect.ShowMessage(
                "Language changed to ${language.toString().replaceFirstChar { it.uppercase() }}"
            )
        )
    }

    private fun openReminderMode() = intent {
        if (state.isReminderEnabled) {
            directions.navigateToReminderModeScreen()
        } else {
            postSideEffect(
                SettingScreenContract.SideEffect.ShowMessage("Please enable reminders first")
            )
        }
    }

    private fun comingSoonMessage(message: String) = intent {
        postSideEffect(
            SettingScreenContract.SideEffect.ShowMessage(message)
        )
    }

    private fun toggleReminders(enabled: Boolean) = intent {
        appRepository.toggleReminders(enabled)
        reduce {
            state.copy(isReminderEnabled = enabled)
        }
        postSideEffect(
            SettingScreenContract.SideEffect.ShowMessage(
                if (enabled) "Reminders enabled" else "Reminders disabled"
            )
        )
    }
}
