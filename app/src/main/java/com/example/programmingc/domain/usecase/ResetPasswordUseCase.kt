package com.example.programmingc.domain.usecase

import com.example.programmingc.domain.model.Credential
import com.example.programmingc.domain.repo.ICredentialRepository
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val credentialRepository: ICredentialRepository
) {
    suspend operator fun invoke(email: String): Boolean{
        return credentialRepository.resetPassword(email)
    }
}