package com.example.programmingc.domain.repo

import com.example.programmingc.domain.model.Credential
import com.google.firebase.auth.FirebaseUser

interface ICredentialRepository {
    suspend fun authenticate(credential: Credential): Boolean

    suspend fun createAcc(credential: Credential): FirebaseUser?

    suspend fun resetPassword(email: String): Boolean
}