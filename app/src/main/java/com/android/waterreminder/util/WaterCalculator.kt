package com.android.waterreminder.util

import android.annotation.SuppressLint
import com.android.waterreminder.data.datasource.local.database.UserProfileEntity

data class WaterGoal(
    val totalWaterMl: Int,
    val waterPerGlass: Int = 250
)

object WaterCalculator {

    @SuppressLint("DefaultLocale")
    fun calculateDailyWaterGoal(userInfo: UserProfileEntity): WaterGoal {
        val ageMultiplier = when {
            userInfo.age > 55 -> 30
            userInfo.age > 30 -> 33
            else -> 35
        }

        val genderMultiplier = when (userInfo.gender.lowercase()) {
            "male" -> ageMultiplier
            "female" -> (ageMultiplier * 0.88).toInt()
            else -> (ageMultiplier * 0.94).toInt()
        }

        val heightFactor = 1.0 + ((userInfo.height - 160).coerceAtLeast(0) * 0.005)

        val baseMl = (userInfo.weight * genderMultiplier * heightFactor).toInt()

        val roundedMl = (baseMl / 100) * 100

        return WaterGoal(
            totalWaterMl = roundedMl.coerceIn(1500, 4000),
            waterPerGlass = 250
        )
    }
}