package com.example.programmingc.domain.repo

import com.example.programmingc.domain.model.User

interface IUserRepository {
    suspend fun insert(user: User)

    suspend fun updateNotActiveUser(userId: String)

    suspend fun getCurrentUser(): User?
}