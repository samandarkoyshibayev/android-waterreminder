package com.android.waterreminder.data.datasource.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DailyWaterRecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: DailyWaterRecord)

    @Query("SELECT * FROM daily_water_records WHERE date = :date")
    suspend fun getRecordByDate(date: String): DailyWaterRecord?

    @Query("SELECT * FROM daily_water_records ORDER BY date DESC LIMIT :limit")
    suspend fun getRecentRecords(limit: Int): List<DailyWaterRecord>

    @Query("SELECT * FROM daily_water_records WHERE date >= :startDate AND date <= :endDate ORDER BY date ASC")
    suspend fun getRecordsBetweenDates(startDate: String, endDate: String): List<DailyWaterRecord>

    @Query("DELETE FROM daily_water_records WHERE date < :date")
    suspend fun deleteOldRecords(date: String)
}