package com.android.waterreminder.presentation.screens.analysis

import android.content.Context
import androidx.lifecycle.ViewModel
import com.android.waterreminder.data.datasource.local.preference.AppPreferenceHelper
import com.android.waterreminder.domain.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class AnalysisScreenViewModel @Inject constructor(
    private val appPreferenceHelper: AppPreferenceHelper,
    private val appRepository: AppRepository,
) : AnalysisScreenContract.VieModel, ViewModel() {

    override val container =
        container<AnalysisScreenContract.UIState, AnalysisScreenContract.SideEffect>(
            AnalysisScreenContract.UIState()
        )

    override fun onEventDispatcher(intent: AnalysisScreenContract.Intent) {
        when(intent){
            is AnalysisScreenContract.Intent.LoadData -> loadData()
        }
    }

    private fun loadData() {
        val targetWaterAmount = appPreferenceHelper.getTotalWaterGoal()
        val totalDrunkWaterAmount = appPreferenceHelper.getDrunkWaterToday()
        intent {
            appRepository.getBodyInfo().collect { result ->
                result.onSuccess { userData ->
                    val height = (userData?.height ?: 0)
                    val weight = (userData?.weight ?: 0)
                    val bmi = if (height > 0) {
                        weight / ((height / 100f) * (height / 100f))
                    } else 0f

                    reduce {
                        state.copy(
                            targetWater = targetWaterAmount,
                            totalDrunkWater = totalDrunkWaterAmount,
                            bmi = bmi
                        )
                    }
                }
            }
        }
    }
}