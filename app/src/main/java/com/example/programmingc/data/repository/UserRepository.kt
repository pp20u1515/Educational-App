package com.example.programmingc.data.repository

import com.example.programmingc.data.datasource.local.service.UserDaoService
import com.example.programmingc.data.datasource.remote.service.INetworkDaoService
import com.example.programmingc.domain.model.User
import com.example.programmingc.domain.repo.IUserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDaoService: UserDaoService,
    private val networkDaoService: INetworkDaoService
): IUserRepository {

    override suspend fun insert(user: User) {
        userDaoService.insert(user)
        networkDaoService
    }

    override suspend fun read() {
        TODO("Not yet implemented")
    }

    override suspend fun delete() {
        TODO("Not yet implemented")
    }

    override suspend fun update() {

    }

    override fun getAll(): Flow<List<User>> {
        TODO("Not yet implemented")
        //return userDaoService.readAll()
    }
}