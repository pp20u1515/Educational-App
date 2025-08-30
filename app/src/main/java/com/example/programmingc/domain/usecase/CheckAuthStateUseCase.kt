package com.example.programmingc.domain.usecase

import com.example.programmingc.data.repository.AuthRepository
import javax.inject.Inject
import kotlin.Result

class CheckAuthStateUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<Boolean> {
        return try {
            Result.success(authRepository.isUserAuthenticated())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}