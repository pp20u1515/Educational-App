package com.example.databasedependencies.db

import com.example.databasedependencies.dao.CredentialDao
import com.example.databasedependencies.dao.UserDao
import com.example.databasedependencies.entity.CredentialEntity
import com.example.databasedependencies.entity.UserEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.Flow

class Database {
    private val userStorage = MutableStateFlow<List<UserEntity>>(
        value = mutableListOf()
    )
    private val credentialStorage = MutableStateFlow<List<CredentialEntity>>(
        value = mutableListOf()
    )

    fun getUserDao(): UserDao {
        return object: UserDao{
            override suspend fun insert(user: UserEntity) {
                val currentUsers = userStorage.value
                userStorage.emit(currentUsers + user)
            }

            override suspend fun insertAll(users: List<UserEntity>){
                val currentUsers = userStorage.value
                userStorage.emit(currentUsers + users)
            }

            override fun readAll(): Flow<List<UserEntity>> {
                return userStorage
            }
        }
    }

    fun getCredentialDao(): CredentialDao{
        return object: CredentialDao{
            override suspend fun insert(credential: CredentialEntity){
                val currentCredentials = credentialStorage.value
                credentialStorage.emit(currentCredentials + credential)
            }

            override suspend fun read(): CredentialEntity? {
                return credentialStorage.value.firstOrNull()
            }
        }
    }
}