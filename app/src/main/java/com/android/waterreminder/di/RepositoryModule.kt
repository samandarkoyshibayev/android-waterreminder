package com.android.waterreminder.di

import com.android.waterreminder.data.repository.AppRepositoryImpl
import com.android.waterreminder.domain.repository.AppRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun provideAppRepository(impl: AppRepositoryImpl): AppRepository
}