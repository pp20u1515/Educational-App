package com.example.programmingc.data.datasource.local.service

import com.example.databasedependencies.db.Database
import com.example.programmingc.data.datasource.local.mapper.toDomain
import com.example.programmingc.data.datasource.local.mapper.toEntity
import com.example.programmingc.domain.model.User
import javax.inject.Inject

class UserDaoService @Inject constructor(database: Database) {
    private val userDao = database.getUserDao()

    suspend fun insert(user: User){
        userDao.insert(user.toEntity())
    }

    suspend fun readByEmail(email: String): User?{
        return userDao.readByEmail(email)?.toDomain()
    }

    suspend fun update(user: User){
        return userDao.update(user.toEntity())
    }

    suspend fun delete(user: User){
        return userDao.delete(user.toEntity())
    }

    suspend fun getCurrentUser(): User?{
        return userDao.getCurrentUser()?.toDomain()
    }

    suspend fun updateActiveUser(userId: String){
        userDao.updateActiveUser(userId)
    }

    suspend fun updateNotActiveUser(userId: String){
        userDao.updateNotActiveUser(userId)
    }
}