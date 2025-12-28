package com.android.waterreminder.presentation.screens.home

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.waterreminder.data.datasource.local.preference.AppPreferenceHelper
import com.android.waterreminder.domain.repository.AppRepository
import com.android.waterreminder.util.WaterReminderScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.viewmodel.container
import java.time.LocalTime
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class HomeScreenViewmodel @Inject constructor(
    private val appRepository: AppRepository,
    private val appPreferenceHelper: AppPreferenceHelper,
    private val reminderScheduler: WaterReminderScheduler
) : HomeScreenContract.ViewModel, ViewModel() {


    override val container = container<HomeScreenContract.UIState, HomeScreenContract.SideEffect>(
        HomeScreenContract.UIState()
    )

    override fun onEventDispatcher(intent: HomeScreenContract.Intent) {
        when (intent) {
            HomeScreenContract.Intent.LoadData -> loadData()
            is HomeScreenContract.Intent.AddDrink -> addDrink(intent.amount)
            HomeScreenContract.Intent.ShowReminder -> {
                reminderScheduler.scheduleImmediateReminder()
            }
        }
    }

    private fun loadData() = intent {
        try {
            appRepository.getBodyInfo().collect { result ->
                result.onSuccess { userProfile ->
                    if (userProfile != null) {
                        val drunkWater = appPreferenceHelper.getDrunkWaterToday()
                        val totalWater = appPreferenceHelper.getTotalWaterGoal()
                        val lastAmount = appPreferenceHelper.getLastDrinkAmount()
                        val lastTimeString = appPreferenceHelper.getLastDrinkTime()

                        val progressPercent = if (totalWater > 0) {
                            ((drunkWater.toFloat() / totalWater) * 100).toInt().coerceIn(0, 100)
                        } else 0

                        val isGoalReached = drunkWater >= totalWater

                        val lastTime = if (lastTimeString.isNotEmpty()) {
                            LocalTime.parse(lastTimeString)
                        } else null

                        reduce {
                            state.copy(
                                fullName = appPreferenceHelper.getUserName() ?: "User",
                                totalWaterMl = totalWater,
                                drunkWaterMl = drunkWater,
                                progressPercent = progressPercent,
                                lastDrinkAmount = lastAmount,
                                lastDrinkTime = lastTime,
                                isGoalReached = isGoalReached,
                                canAddDrink = !isGoalReached
                            )
                        }
                    }
                }.onFailure { error ->
                    Log.e("HomeScreen", "Error loading data: ${error.message}")
                    postSideEffect(
                        HomeScreenContract.SideEffect.ShowMessage(
                            "Error loading data"
                        )
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("HomeScreen", "Exception: ${e.message}")
            postSideEffect(
                HomeScreenContract.SideEffect.ShowMessage(
                    "An error occurred"
                )
            )
        }
    }

    @SuppressLint("NewApi")
    private fun addDrink(amount: Int) = intent {
        val currentDrunk = state.drunkWaterMl
        val newDrunk = (currentDrunk + amount).coerceAtMost(state.totalWaterMl)
        val newProgress = if (state.totalWaterMl > 0) {
            ((newDrunk.toFloat() / state.totalWaterMl) * 100).toInt().coerceIn(0, 100)
        } else 0

        val isGoalReached = newDrunk >= state.totalWaterMl

        appPreferenceHelper.saveDrunkWaterToday(newDrunk)
        appPreferenceHelper.saveLastDrinkAmount(amount)
        appPreferenceHelper.saveLastDrinkTime(LocalTime.now().toString())

        reduce {
            state.copy(
                drunkWaterMl = newDrunk,
                progressPercent = newProgress,
                lastDrinkAmount = amount,
                lastDrinkTime = LocalTime.now(),
                isGoalReached = isGoalReached,
                canAddDrink = !isGoalReached
            )
        }

        if (isGoalReached) {
            postSideEffect(
                HomeScreenContract.SideEffect.ShowMessage(
                    "Congratulations! You've reached your daily goal! 🎉"
                )
            )
        } else {
            postSideEffect(
                HomeScreenContract.SideEffect.ShowMessage(
                    "Great! You drank $amount ml of water"
                )
            )
        }
    }
}