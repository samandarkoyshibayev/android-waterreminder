package com.android.waterreminder.di

import com.android.waterreminder.presentation.screens.bodyinfo.BodyInfoScreenDirections
import com.android.waterreminder.presentation.screens.bodyinfo.BodyInfoScreenDirectionsImpl
import com.android.waterreminder.presentation.screens.edit.directions.EditScreenDirections
import com.android.waterreminder.presentation.screens.edit.directions.EditScreenDirectionsImpl
import com.android.waterreminder.presentation.screens.onboarding.OnboardingDirections
import com.android.waterreminder.presentation.screens.onboarding.OnboardingDirectionsImpl
import com.android.waterreminder.presentation.screens.profile.directions.ProfileScreenDirections
import com.android.waterreminder.presentation.screens.profile.directions.ProfileScreenDirectionsImpl
import com.android.waterreminder.presentation.screens.reminder_mode.ReminderModeScreenDirections
import com.android.waterreminder.presentation.screens.reminder_mode.ReminderModeScreenDirectionsImpl
import com.android.waterreminder.presentation.screens.setting.SettingsScreenDirections
import com.android.waterreminder.presentation.screens.setting.SettingsScreenDirectionsImpl
import com.android.waterreminder.presentation.screens.splash.SplashScreenDirections
import com.android.waterreminder.presentation.screens.splash.SplashScreenDirectionsImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DirectionsModule {
    @Binds
    fun provideSplashScreenDirections(impl: SplashScreenDirectionsImpl): SplashScreenDirections

    @Binds
    fun provideOnboardingScreenDirections(impl: OnboardingDirectionsImpl): OnboardingDirections

    @Binds
    fun provideBodyInfoScreenDirections(impl: BodyInfoScreenDirectionsImpl): BodyInfoScreenDirections

    @Binds
    fun provideSettingScreenDirections(impl: SettingsScreenDirectionsImpl): SettingsScreenDirections

    @Binds
    fun provideEditScreenDirections(impl: EditScreenDirectionsImpl): EditScreenDirections

    @Binds
    fun provideProfileScreenDirections(impl: ProfileScreenDirectionsImpl): ProfileScreenDirections

    @Binds
    fun provideReminderModeScreenDirections(impl: ReminderModeScreenDirectionsImpl): ReminderModeScreenDirections

}