package com.example.programmingc.domain.usecase

import com.example.programmingc.data.repository.AuthRepository
import javax.inject.Inject
import kotlin.Result


class SignOutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<Boolean> {
        return try {
            authRepository.signOut()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}