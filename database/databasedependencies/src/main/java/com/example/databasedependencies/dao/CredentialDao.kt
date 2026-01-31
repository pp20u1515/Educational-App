package com.example.databasedependencies.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.databasedependencies.entity.CredentialEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CredentialDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(credentialDao: CredentialEntity)

    @Query("SELECT * FROM Credentials WHERE email=:email")
    suspend fun readByEmail(email: String): CredentialEntity?

    @Delete
    suspend fun delete(credentialDao: CredentialEntity)

    @Update
    suspend fun update(credentialDao: CredentialEntity)
}