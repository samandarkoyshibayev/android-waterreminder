package com.android.waterreminder.data.datasource.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserInfo(info: UserProfileEntity)

    @Query("SELECT * FROM user_profile ORDER BY id DESC LIMIT 1")
    suspend fun getLatestUserInfo(): UserProfileEntity?

    @Update
    suspend fun updateUserInfo(info: UserProfileEntity)

}