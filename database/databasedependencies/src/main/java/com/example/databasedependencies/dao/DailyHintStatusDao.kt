package com.example.databasedependencies.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.databasedependencies.entity.DailyHintStatusEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyHintStatusDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dailyHintStatus: DailyHintStatusEntity)

    @Query("SELECT * FROM DailyHintStatus")
    fun readAll(): Flow<List<DailyHintStatusEntity>>

    @Update
    suspend fun update(dailyHintStatus: DailyHintStatusEntity)

    @Delete
    suspend fun delete(dailyHintStatus: DailyHintStatusEntity)
}