package com.android.waterreminder.data.datasource.local.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [UserProfileEntity::class, DailyWaterRecord::class],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userInfoDao(): UserInfoDao
    abstract fun dailyWaterRecordDao(): DailyWaterRecordDao
}