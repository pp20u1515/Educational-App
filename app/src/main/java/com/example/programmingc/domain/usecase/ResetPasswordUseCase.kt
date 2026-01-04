package com.example.programmingc.domain.usecase

import com.example.programmingc.domain.repo.IAuthRepository
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val authRepository: IAuthRepository
) {
    suspend operator fun invoke(email: String): Boolean{
        return authRepository.resetPassword(email)
    }
}