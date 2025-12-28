package com.android.waterreminder.presentation.screens.profile.directions

import com.android.waterreminder.presentation.screens.edit.EditScreen
import com.android.waterreminder.ui.navigation.AppNavigator
import javax.inject.Inject

class ProfileScreenDirectionsImpl @Inject constructor(
    private val appNavigator: AppNavigator
) : ProfileScreenDirections {

    override suspend fun navigateToEditScreen() {
        appNavigator.push(EditScreen())
    }
}