package com.android.waterreminder.di

import com.android.waterreminder.data.datasource.local.preference.AppPreferenceHelper
import com.android.waterreminder.data.datasource.local.preference.AppPreferenceHelperImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PreferenceModule {

    @Binds
    @Singleton
    abstract fun bindAppPreferenceHelper(
        impl: AppPreferenceHelperImpl
    ): AppPreferenceHelper
}