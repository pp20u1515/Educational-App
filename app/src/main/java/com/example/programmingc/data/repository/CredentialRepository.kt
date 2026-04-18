package com.example.programmingc.data.repository

import com.example.programmingc.data.source.local.service.CredentialDaoService
import com.example.programmingc.data.source.local.service.UserDaoService
import com.example.programmingc.data.source.remote.mapper.toDomain
import com.example.programmingc.data.source.remote.mapper.toDto
import com.example.programmingc.data.source.remote.service.INetworkDataSource
import com.example.programmingc.domain.model.Credential
import com.example.programmingc.domain.repo.ICredentialRepository
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class CredentialRepository @Inject constructor(
    private val credentialDaoService: CredentialDaoService,
    private val userDaoService: UserDaoService,
    private val networkDataSource: INetworkDataSource
): ICredentialRepository{
    override suspend fun authenticate(credential: Credential): Boolean {
        var rc = true
        val user = networkDataSource.authenticate(credential.toDto())

        if (user.isFailure){
            rc = false
        }
        else{
            // TODO check the return type in userDaoService
            userDaoService.insert(user.getOrNull()!!.toDomain())
            credentialDaoService.insert(credential)
        }
        return rc
    }

    override suspend fun createAcc(credential: Credential): Result<FirebaseUser> {
        val rc = networkDataSource.registerUser(credential.toDto())

        if (rc != null){
            credentialDaoService.insert(credential)
        }

        return rc
    }

    override suspend fun resetPassword(email: String): Result<Boolean> {
        return networkDataSource.resetPassword(email)
    }
}