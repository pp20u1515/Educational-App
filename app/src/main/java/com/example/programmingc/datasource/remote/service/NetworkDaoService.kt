package com.example.programmingc.datasource.remote.service

import com.example.programmingc.data.datasource.remote.service.INetworkDaoService
import com.example.programmingc.datasource.remote.dao.FirebaseAuthDao
import com.example.programmingc.datasource.remote.mapper.toDomain
import com.example.programmingc.datasource.remote.mapper.toDto
import com.example.programmingc.domain.model.Credential
import com.example.programmingc.domain.model.User
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class NetworkDaoService @Inject constructor(
    private val firebaseAuth: FirebaseAuthDao
): INetworkDaoService {

    override suspend fun authenticate(credential: Credential): User? {
        return firebaseAuth.authenticate(credential.toDto())?.toDomain()
    }

    override suspend fun register(credential: Credential): FirebaseUser? {
        return firebaseAuth.register(credential.toDto())
    }

    override suspend fun resetPassword(email: String): Boolean {
        return firebaseAuth.resetPassword(email)
    }
}