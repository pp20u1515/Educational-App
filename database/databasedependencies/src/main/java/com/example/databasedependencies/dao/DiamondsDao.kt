package com.example.databasedependencies.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.example.databasedependencies.entity.DiamondsEntity

@Dao
interface DiamondsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(diamonds: DiamondsEntity)

    @Update
    suspend fun update(diamonds: DiamondsEntity)

    @Delete
    suspend fun delete(diamonds: DiamondsEntity)
}