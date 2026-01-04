package com.example.programmingc.domain.usecase

import com.example.programmingc.domain.model.Credential
import com.example.programmingc.domain.repo.IAuthRepository
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class CreateAccUseCase @Inject constructor(
    private val authRepository: IAuthRepository
) {
    suspend operator fun invoke(credential: Credential): FirebaseUser? {
        return authRepository.createAcc(credential)
    }
}