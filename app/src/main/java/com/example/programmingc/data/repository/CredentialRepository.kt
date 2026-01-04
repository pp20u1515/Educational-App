package com.example.programmingc.data.repository

import com.example.programmingc.data.datasource.local.service.CredentialDaoService
import com.example.programmingc.data.datasource.local.service.UserDaoService
import com.example.programmingc.data.datasource.remote.service.INetworkDaoService
import com.example.programmingc.domain.model.Credential
import com.example.programmingc.domain.repo.ICredentialRepository
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class CredentialRepository @Inject constructor(
    private val credentialDaoService: CredentialDaoService,
    private val userDaoService: UserDaoService,
    private val networkDaoService: INetworkDaoService
): ICredentialRepository{
    override suspend fun authenticate(credential: Credential): Boolean {
        var rc = true
        val user = networkDaoService.authenticate(credential)

        if (user == null){
            rc = false
        }
        else{
            userDaoService.insert(user)
            credentialDaoService.insert(credential)
        }
        return rc
    }

    override suspend fun createAcc(credential: Credential): FirebaseUser? {
        val rc = networkDaoService.register(credential)

        if (rc != null){
            credentialDaoService.insert(credential)
        }

        return rc
    }

    override suspend fun resetPassword(email: String): Boolean {
        return networkDaoService.resetPassword(email)
    }
}