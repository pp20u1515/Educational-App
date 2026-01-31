package com.example.programmingc.data.datasource.local.service

import com.example.databasedependencies.db.Database
import com.example.programmingc.data.datasource.local.mapper.toDomain
import com.example.programmingc.data.datasource.local.mapper.toEntity
import com.example.programmingc.domain.model.Credential
import javax.inject.Inject

class CredentialDaoService @Inject constructor(database: Database) {
    private val credentialDao = database.getCredentialDao()

    suspend fun insert(credential: Credential){
        credentialDao.insert(credential.toEntity())
    }

    suspend fun readByEmail(email: String): Credential?{
        return credentialDao.readByEmail(email)?.toDomain()
    }

    suspend fun delete(credential: Credential){
        credentialDao.delete(credential.toEntity())
    }

    suspend fun update(credential: Credential){
        credentialDao.update(credential.toEntity())
    }
}