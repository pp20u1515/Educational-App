package com.example.databasedependencies.dao

import com.example.databasedependencies.entity.CredentialEntity

interface CredentialDao {
    suspend fun insert(credentialDao: CredentialEntity)
    suspend fun read(): CredentialEntity?
}