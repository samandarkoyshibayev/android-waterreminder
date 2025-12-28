package com.android.waterreminder.di

import android.content.Context
import androidx.room.Room
import com.android.waterreminder.data.datasource.local.database.AppDatabase
import com.android.waterreminder.data.datasource.local.database.UserInfoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "water_reminder_db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideUserInfoDao(db: AppDatabase): UserInfoDao = db.userInfoDao()

    @Provides
    fun provideDailyWaterRecordDao(database: AppDatabase) = database.dailyWaterRecordDao()

}