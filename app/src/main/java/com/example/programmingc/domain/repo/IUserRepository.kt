package com.example.programmingc.domain.repo

import com.example.programmingc.domain.model.User
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    suspend fun update()
    fun getAll(): Flow<List<User>>
}