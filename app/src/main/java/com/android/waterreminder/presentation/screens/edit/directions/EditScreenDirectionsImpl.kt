package com.android.waterreminder.presentation.screens.edit.directions

import com.android.waterreminder.ui.navigation.AppNavigator
import javax.inject.Inject

class EditScreenDirectionsImpl @Inject constructor(
    val appNavigator: AppNavigator
): EditScreenDirections{

    override suspend fun back() {
        appNavigator.back()
    }
}