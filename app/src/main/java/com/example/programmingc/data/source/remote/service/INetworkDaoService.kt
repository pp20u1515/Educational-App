package com.example.programmingc.data.source.remote.service

import com.example.programmingc.domain.model.Credential
import com.example.programmingc.domain.model.Diamonds
import com.example.programmingc.domain.model.Live
import com.example.programmingc.domain.model.User
import com.google.firebase.auth.FirebaseUser

interface INetworkDaoService {
    suspend fun authenticate(credential: Credential): User?

    suspend fun register(credential: Credential): FirebaseUser?

    suspend fun initializeUserResources(userId: String): Boolean

    suspend fun resetPassword(email: String): Boolean

    suspend fun getLivesByUserId(userId: String): Live?

    suspend fun getDiamondsByUserId(userId: String): Diamonds?
}