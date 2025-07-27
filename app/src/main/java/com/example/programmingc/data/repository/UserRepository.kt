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
    override suspend fun update() {
        /*val users = networkDaoService.getUsers()

        if (users == null){
            error("Ошибка обновления данных")
        }
        else{
            userDaoService.insertAll(users)
        }*/
    }

    override fun getAll(): Flow<List<User>> {
        return userDaoService.readAll()
    }
}