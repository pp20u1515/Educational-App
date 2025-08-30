package com.example.programmingc.domain.repo

interface IAuthRepository {
    suspend fun isUserAuthenticated(): Boolean
    suspend fun signOut(): Boolean
}