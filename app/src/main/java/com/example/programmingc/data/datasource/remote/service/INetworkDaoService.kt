package com.example.programmingc.data.datasource.remote.service

import com.example.programmingc.domain.model.Credential
import com.example.programmingc.domain.model.User

interface INetworkDaoService {
    suspend fun authenticate(credential: Credential): User?
}