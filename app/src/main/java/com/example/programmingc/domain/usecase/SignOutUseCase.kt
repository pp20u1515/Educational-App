package com.example.programmingc.domain.usecase

import com.example.programmingc.domain.repo.IAuthRepository
import javax.inject.Inject


class SignOutUseCase @Inject constructor(
    private val authRepository: IAuthRepository
) {
    suspend operator fun invoke(): Boolean {
        return authRepository.signOut()
    }
}