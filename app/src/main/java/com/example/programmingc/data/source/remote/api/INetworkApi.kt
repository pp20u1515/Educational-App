package com.example.programmingc.data.source.remote.api

import com.example.programmingc.data.source.remote.model.CredentialDto

interface INetworkApi {
    suspend fun authenticate(credential: CredentialDto)
}