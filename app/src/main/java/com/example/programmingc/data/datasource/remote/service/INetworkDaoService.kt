package com.example.programmingc.data.datasource.remote.service

import com.example.programmingc.domain.model.Credential
import com.example.programmingc.domain.model.User
import com.google.firebase.auth.FirebaseUser

interface INetworkDaoService {
    suspend fun authenticate(credential: Credential): User?

    suspend fun register(credential: Credential): FirebaseUser?

    suspend fun resetPassword(email: String): Boolean
}