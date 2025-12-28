package com.android.waterreminder.di

import com.android.waterreminder.ui.navigation.AppNavigationDispatcher
import com.android.waterreminder.ui.navigation.AppNavigator
import com.android.waterreminder.ui.navigation.NavigationHandler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface NavigationModule {

    @Binds
    fun provideAppNavigator(impl: AppNavigationDispatcher): AppNavigator

    @Binds
    fun navigationHandler(impl: AppNavigationDispatcher): NavigationHandler
}