package com.example.programmingc.domain.repo

import com.example.programmingc.domain.model.Credential

interface ICredentialRepository {
    suspend fun authenticate(credential: Credential)
}