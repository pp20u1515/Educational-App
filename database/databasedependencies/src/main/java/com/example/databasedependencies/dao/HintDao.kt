package com.example.databasedependencies.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.databasedependencies.entity.HintEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HintDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(hint: HintEntity)

    @Query("SELECT * FROM Hints")
    fun readAll(): Flow<List<HintEntity>>

    @Update
    suspend fun update(hint: HintEntity)

    @Delete
    suspend fun delete(hint: HintEntity)
}