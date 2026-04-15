package com.example.programmingc.data.source.remote.service

import com.example.programmingc.data.source.remote.dao.FirebaseAuthDao
import com.example.programmingc.data.source.remote.dao.FirebaseStorageDao
import com.example.programmingc.data.source.remote.mapper.toDomain
import com.example.programmingc.data.source.remote.mapper.toDto
import com.example.programmingc.domain.model.Credential
import com.example.programmingc.domain.model.Diamonds
import com.example.programmingc.domain.model.Live
import com.example.programmingc.domain.model.User
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class NetworkDaoService @Inject constructor(
    private val firebaseAuth: FirebaseAuthDao,
    private val firebaseStorage: FirebaseStorageDao
): INetworkDaoService {

    override suspend fun authenticate(credential: Credential): User? {
        return firebaseAuth.authenticate(credential.toDto())?.toDomain()
    }

    override suspend fun register(credential: Credential): FirebaseUser? {
        return firebaseAuth.register(credential.toDto())
    }

    override suspend fun initializeUserResources(userId: String): Boolean {
        return firebaseStorage.register(userId)
    }

    override suspend fun resetPassword(email: String): Boolean {
        return firebaseAuth.resetPassword(email)
    }

    override suspend fun getLivesByUserId(userId: String): Live? {
        return firebaseStorage.getLivesByUserId(userId)
    }

    override suspend fun getDiamondsByUserId(userId: String): Diamonds? {
        return firebaseStorage.getDiamondsByUserId(userId)
    }
}