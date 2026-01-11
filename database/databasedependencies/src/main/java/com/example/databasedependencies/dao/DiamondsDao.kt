package com.example.databasedependencies.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.databasedependencies.entity.DiamondsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DiamondsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(diamonds: DiamondsEntity)

    @Query("SELECT * FROM Diamonds")
    fun readAll(): Flow<List<DiamondsEntity>>

    @Update
    suspend fun update(diamonds: DiamondsEntity)

    @Delete
    suspend fun delete(diamonds: DiamondsEntity)
}