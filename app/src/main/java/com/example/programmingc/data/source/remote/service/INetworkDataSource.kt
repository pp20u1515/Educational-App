package com.example.programmingc.data.source.remote.service

import com.example.programmingc.data.source.remote.model.CredentialDto
import com.example.programmingc.data.source.remote.model.DiamondsDto
import com.example.programmingc.data.source.remote.model.LiveDto
import com.example.programmingc.data.source.remote.model.UserDto
import com.google.firebase.auth.FirebaseUser

interface INetworkDataSource {
    suspend fun authenticate(credential: CredentialDto): Result<UserDto>
    suspend fun registerUser(credential: CredentialDto): Result<FirebaseUser>
    suspend fun resetPassword(email: String): Result<Boolean>
    suspend fun registerUserResources(userId: String): Result<Boolean>
    suspend fun getLivesByUserId(userId: String): Result<LiveDto>
    suspend fun getDiamondsByUserId(userId: String): Result<DiamondsDto>
}