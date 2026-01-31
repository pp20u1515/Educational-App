package com.example.programmingc.data.repository

import com.example.programmingc.data.datasource.local.service.UserDaoService
import com.example.programmingc.domain.model.User
import com.example.programmingc.domain.repo.IUserRepository
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDaoService: UserDaoService
): IUserRepository {

    override suspend fun insert(user: User) {
        userDaoService.insert(user)
    }

    override suspend fun updateNotActiveUser(userId: String) {
        userDaoService.updateNotActiveUser(userId)
    }

    override suspend fun getCurrentUser(): User? {
        return userDaoService.getCurrentUser()
    }
}