package com.example.programmingc.domain.usecase

import com.example.programmingc.domain.model.Credential
import com.example.programmingc.domain.repo.ICredentialRepository
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class CreateAccUseCase @Inject constructor(
    private val credentialRepository: ICredentialRepository
) {
    suspend operator fun invoke(credential: Credential): FirebaseUser? {
        return credentialRepository.createAcc(credential)
    }
}