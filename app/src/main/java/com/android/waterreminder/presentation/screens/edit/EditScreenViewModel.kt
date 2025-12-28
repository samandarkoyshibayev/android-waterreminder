package com.android.waterreminder.presentation.screens.edit

import android.util.Log
import androidx.lifecycle.ViewModel
import com.android.waterreminder.data.datasource.local.database.UserProfileEntity
import com.android.waterreminder.data.datasource.local.preference.AppPreferenceHelper
import com.android.waterreminder.domain.repository.AppRepository
import com.android.waterreminder.presentation.screens.edit.directions.EditScreenDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class EditScreenViewModel @Inject constructor(
    private val repository: AppRepository,
    private val directions: EditScreenDirections,
    private val appPreferenceHelper: AppPreferenceHelper
) : ViewModel(), EditScreenContract.ViewModel {

    override val container: Container<EditScreenContract.UIState, EditScreenContract.SideEffect> =
        container(EditScreenContract.UIState())

    init {
        intent {
            repository.getBodyInfo().collect { result ->
                result.onSuccess { profile ->
                    profile?.let {
                        reduce {
                            state.copy(
                                age = it.age.toString(),
                                weight = it.weight,
                                height = it.height,
                                wakeUpTime = it.wakeUpTime,
                                sleepTime = it.sleepTime,
                                firstName = appPreferenceHelper.getUserName() ?: "",
                                lastName = appPreferenceHelper.getUserLastName() ?: "",
                                gender = it.gender
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onEventDispatcher(intent: EditScreenContract.Intent) {
        intent {
            when (intent) {
                EditScreenContract.Intent.SaveChanges -> {
                    saveChanges()
                }
                EditScreenContract.Intent.Back -> navigateBack()
                is EditScreenContract.Intent.OnAgeChange -> {
                    reduce { state.copy(age = intent.value) }
                }

                is EditScreenContract.Intent.OnFirstNameChange -> {
                    reduce { state.copy(firstName = intent.value) }
                }

                is EditScreenContract.Intent.OnLastNameChange -> {
                    reduce { state.copy(lastName = intent.value) }
                }

                is EditScreenContract.Intent.OnGenderChange -> {
                    reduce { state.copy(gender = intent.value) }
                }

                is EditScreenContract.Intent.OnHeightChange -> {
                    reduce { state.copy(height = intent.value.toIntOrNull()?:0) }
                }

                is EditScreenContract.Intent.OnWeightChange -> {
                    reduce { state.copy(weight = intent.value.toIntOrNull()?:0) }
                }

                is EditScreenContract.Intent.OnSleepTimeChange -> {
                    reduce { state.copy(sleepTime = intent.value) }
                }

                is EditScreenContract.Intent.OnWakeUpTimeChange -> {
                    reduce { state.copy(wakeUpTime = intent.value) }
                }

            }
        }
    }

    private fun saveChanges() = intent {

        Log.d("LOG","SAVE CHANGE ")

        val latestInfo = repository.getBodyInfo().firstOrNull()?.getOrNull()

        val updatedProfile = UserProfileEntity(
            id = latestInfo?.id ?: 0,
            age = state.age.toIntOrNull()?:0,
            gender = state.gender,
            weight = state.weight,
            height = state.height,
            wakeUpTime = state.wakeUpTime,
            sleepTime = state.sleepTime
        )

        appPreferenceHelper.saveUserName(state.firstName)
        appPreferenceHelper.saveUserLastName(state.lastName)

        repository.updateBodyInfo(updatedProfile)
        postSideEffect(EditScreenContract.SideEffect.OnSaved)
        navigateBack()
    }

    private fun navigateBack() = intent {
        directions.back()
    }
}
