package com.android.waterreminder.data.datasource.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val gender: String,
    val age: Int,
    val height: Int,
    val weight: Int,
    val wakeUpTime: String,
    val sleepTime: String,
)