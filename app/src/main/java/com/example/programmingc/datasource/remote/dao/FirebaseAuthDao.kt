package com.example.programmingc.datasource.remote.dao

import com.example.programmingc.datasource.remote.model.CredentialDto
import com.example.programmingc.domain.model.Credential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthDao @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    suspend fun authenticate(credential: CredentialDto): FirebaseUser? {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(credential.email, credential.password).await()
            return result.user
        } catch (e: Exception) {
            null
        }
    }

    suspend fun register(credential: CredentialDto): FirebaseUser?{
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(credential.email, credential.password).await()
            result.user
        }
        catch (e: Exception){
            null
        }
    }

    suspend fun resetPassword(email: String): Boolean{
        return try {
            firebaseAuth.sendPasswordResetEmail(email)
            true
        }
        catch (e: Exception){
            false
        }
    }
}