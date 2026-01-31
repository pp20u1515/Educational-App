package com.example.programmingc.domain.repo

import com.example.programmingc.domain.model.User
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    suspend fun update()
    suspend fun insert(user: User)
    suspend fun read()
    suspend fun delete()
    fun getAll(): Flow<List<User>>

    suspend fun updateNotActiveUser(userId: String)

    suspend fun getCurrentUser(): User?
}