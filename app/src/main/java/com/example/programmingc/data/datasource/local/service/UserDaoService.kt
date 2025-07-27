package com.example.programmingc.data.datasource.local.service

import com.example.databasedependencies.db.Database
import com.example.programmingc.data.datasource.local.mapper.toDomain
import com.example.programmingc.data.datasource.local.mapper.toEntity
import com.example.programmingc.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserDaoService @Inject constructor(database: Database) {
    private val userDao = database.getUserDao()

    suspend fun insert(user: User){
        userDao.insert(user.toEntity())
    }

    suspend fun insertAll(users: List<User>){
        userDao.insertAll(users.map { it.toEntity() })
    }

    fun readAll(): Flow<List<User>> {
        return userDao.readAll().map { users-> users.map { it.toDomain() } }
    }
}