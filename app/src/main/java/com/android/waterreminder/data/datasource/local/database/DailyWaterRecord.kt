package com.android.waterreminder.data.datasource.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "daily_water_records")
data class DailyWaterRecord(
    @PrimaryKey
    val date: String,              // "2025-01-15"
    val waterConsumedMl: Int,      // 1500
    val goalMl: Int,               // 2000
    val percentageCompleted: Int,  // 75
)