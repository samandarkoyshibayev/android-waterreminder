package com.android.waterreminder.presentation.screens.profile

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.android.waterreminder.data.datasource.local.database.UserProfileEntity
import com.android.waterreminder.data.datasource.local.preference.AppPreferenceHelper
import com.android.waterreminder.domain.repository.AppRepository
import com.android.waterreminder.presentation.screens.profile.directions.ProfileScreenDirections
import com.android.waterreminder.util.WaterCalculator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject


@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val repository: AppRepository,
    private val preference: AppPreferenceHelper,
    private val directions: ProfileScreenDirections
) : ProfileScreenContract.ViewModel, ViewModel() {

    override val container =
        container<ProfileScreenContract.UIState, ProfileScreenContract.SideEffect>(
            ProfileScreenContract.UIState()
        )

    init {
        loadProfile()
    }

    private fun loadProfile() = intent {
        val firstname = preference.getUserName() ?: ""
        val lastname = preference.getUserLastName() ?: ""

        repository.getBodyInfo().collect { result ->
            result.onSuccess {
                reduce {
                    state.copy(
                        height = it?.height ?: 0,
                        weight = it?.weight ?: 0,
                        wakeUptime = it?.wakeUpTime ?: "00:00",
                        sleepTime = it?.sleepTime ?: "00:00",
                        firstname = firstname,
                        lastname = lastname,
                        age = it?.age ?: 0,
                        gender = it?.gender ?: "Male"
                    )
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onEventDispatcher(intent: ProfileScreenContract.Intent) {
        when (intent) {
            ProfileScreenContract.Intent.OnEditClicked -> {
                intent {
                    directions.navigateToEditScreen()
                }
            }
        }
    }
}




