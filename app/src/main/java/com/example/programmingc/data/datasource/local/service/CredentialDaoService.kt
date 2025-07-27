package com.example.programmingc.data.datasource.local.service

import com.example.databasedependencies.db.Database
import com.example.programmingc.data.datasource.local.mapper.toDomain
import com.example.programmingc.data.datasource.local.mapper.toEntity
import com.example.programmingc.domain.model.Credential
import javax.inject.Inject

class CredentialDaoService @Inject constructor(database: Database) {
    private val credentialDao = database.getCredentialDao()

    // сохранить данные пользователя (например, email и пароль) в локальную базу.
    suspend fun insert(credential: Credential){
        credentialDao.insert(credential.toEntity())
    }
    // получить сохранённые учетные данные пользователя в виде доменной модели.
    suspend fun read(): Credential?{
        return credentialDao.read()?.toDomain()
    }
}