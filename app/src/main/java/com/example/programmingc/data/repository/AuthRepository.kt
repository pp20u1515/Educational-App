package com.example.programmingc.data.repository

import com.example.programmingc.data.datasource.local.service.UserDaoService
import com.example.programmingc.data.datasource.remote.service.INetworkDaoService
import com.example.programmingc.datasource.remote.mapper.toDomain
import com.example.programmingc.domain.model.Credential
import com.example.programmingc.domain.repo.IAuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val networkDaoService: INetworkDaoService,
    private val userDaoService: UserDaoService
): IAuthRepository {
    override suspend fun isUserAuthenticated(): Boolean {
        val firebaseUser = firebaseAuth.currentUser
        var rc = false

        if (firebaseUser != null){
            rc = true
        }
        return rc
    }

    override suspend fun authenticate(credential: Credential): Boolean {
        var rc = true
        val user = networkDaoService.authenticate(credential)

        if (user == null){
            rc = false
        }
        else{
            userDaoService.insert(user)
        }
        return rc
    }

    override suspend fun createAcc(credential: Credential): FirebaseUser? {
        val user = networkDaoService.register(credential)

        if (user != null){
            userDaoService.insert(user.toDomain())
        }

        return user
    }

    override suspend fun resetPassword(email: String): Boolean {
        return networkDaoService.resetPassword(email)
    }

    override suspend fun signOut(): Boolean {
        return try {
            firebaseAuth.signOut()
            true
        }
        catch (e: Exception){
            false
        }
    }
}