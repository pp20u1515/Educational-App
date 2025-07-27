package com.example.databasedependencies.dao

import com.example.databasedependencies.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserDao {
    suspend fun insert(user: UserEntity)
    suspend fun insertAll(users: List<UserEntity>)
    fun readAll(): Flow<List<UserEntity>>
}