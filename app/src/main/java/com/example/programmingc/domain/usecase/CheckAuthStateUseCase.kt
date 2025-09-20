package com.example.programmingc.domain.usecase

import com.example.programmingc.domain.repo.IAuthRepository
import javax.inject.Inject
import kotlin.Result

class CheckAuthStateUseCase @Inject constructor(
    private val authRepository: IAuthRepository
) {
    suspend operator fun invoke(): Result<Boolean> {
        return try {
            Result.success(authRepository.isUserAuthenticated())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}