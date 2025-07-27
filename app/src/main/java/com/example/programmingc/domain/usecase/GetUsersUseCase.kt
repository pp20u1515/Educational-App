package com.example.programmingc.domain.usecase

import com.example.programmingc.domain.model.User
import com.example.programmingc.domain.repo.IUserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(private val userRepository: IUserRepository) {
    operator fun invoke(): Flow<List<User>> = userRepository.getAll()
}