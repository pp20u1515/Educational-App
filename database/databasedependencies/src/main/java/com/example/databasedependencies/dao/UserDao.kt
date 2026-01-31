package com.example.databasedependencies.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.databasedependencies.entity.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)

    @Query("SELECT * FROM Users WHERE email = :email")
    suspend fun readByEmail(email: String): UserEntity?

    @Query("SELECT * FROM Users WHERE isCurrent = true LIMIT 1")
    suspend fun getCurrentUser(): UserEntity?

    @Update
    suspend fun update(user: UserEntity)

    @Delete
    suspend fun delete(user: UserEntity)

    @Query("UPDATE Users SET isCurrent = true WHERE id = :userId")
    suspend fun updateActiveUser(userId: String)

    @Query("UPDATE Users SET isCurrent = false WHERE id = :userId")
    suspend fun updateNotActiveUser(userId: String)
}