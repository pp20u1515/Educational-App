package com.example.programmingc.domain.usecase

import com.example.programmingc.domain.model.Credential
import com.example.programmingc.domain.repo.IAuthRepository
import javax.inject.Inject

class AuthenticateUseCase @Inject constructor(
    private val authRepository: IAuthRepository
){
    suspend operator fun invoke(credential: Credential): Boolean{
       return authRepository.authenticate(credential)
    }
}