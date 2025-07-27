package com.example.programmingc.data.repository

import com.example.programmingc.data.datasource.local.service.CredentialDaoService
import com.example.programmingc.data.datasource.local.service.UserDaoService
import com.example.programmingc.data.datasource.remote.service.INetworkDaoService
import com.example.programmingc.domain.model.Credential
import com.example.programmingc.domain.repo.ICredentialRepository
import javax.inject.Inject

class CredentialRepository @Inject constructor(
    private val credentialDaoService: CredentialDaoService,
    private val userDaoService: UserDaoService,
    private val networkDaoService: INetworkDaoService
): ICredentialRepository{
    override suspend fun authenticate(credential: Credential) {
        val owner = networkDaoService.authenticate(credential)

        if (owner == null){
            error("Ошибка авторизации")
        }
        else{
            userDaoService.insert(owner)
            credentialDaoService.insert(credential)
        }
    }
}