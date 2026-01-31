package com.example.databasedependencies.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.databasedependencies.entity.HintEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HintDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(hint: HintEntity)

    @Query("SELECT dailyHints FROM Hints WHERE userId = :userId")
    suspend fun getAvailableHints(userId: String): Int

    @Query("SELECT lastResetDate FROM Hints WHERE userId =:userId")
    suspend fun getLastResetDate(userId: String): Long?

    @Query("""
    SELECT COUNT(*) FROM Hints 
    WHERE userId = :userId 
    AND (
        lastResetDate < strftime('%s', 'now', 'localtime', 'start of day') * 1000
        OR
        lastResetDate IS NULL
    )
""")
    suspend fun needsReset(userId: String): Int

    @Query("""
        UPDATE Hints
        SET dailyHints = dailyLimit, lastResetDate = :currentTime
        WHERE userId = :userId
    """)
    suspend fun resetToDailyLimit(userId: String, currentTime: Long): Int

    @Delete
    suspend fun delete(hint: HintEntity)

    @Query("UPDATE HINTS SET dailyHints = dailyHints - 1 WHERE userId = :userId AND dailyHints > 0")
    suspend fun useDailyHint(userId: String): Int

    @Query("SELECT dailyHints FROM Hints WHERE userId = :userId")
    fun observeAvailableHints(userId: String): Flow<Int>
}