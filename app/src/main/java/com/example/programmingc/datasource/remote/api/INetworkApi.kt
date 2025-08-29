package com.example.programmingc.datasource.remote.api

import com.example.programmingc.datasource.remote.model.CredentialDto

interface INetworkApi {
    suspend fun authenticate(credential: CredentialDto)
}