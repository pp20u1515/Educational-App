package com.example.programmingc.data.repository

import com.example.programmingc.domain.repo.IAuthRepository
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): IAuthRepository {
    override suspend fun isUserAuthenticated(): Boolean {
        val firebaseUser = firebaseAuth.currentUser
        var rc = false
        if (firebaseUser != null){
            rc = true
        }
        return rc
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