package com.example.programmingc.datasource.remote.service

import com.example.programmingc.data.datasource.remote.service.INetworkDaoService
import com.example.programmingc.datasource.remote.dao.NetworkDao
import com.example.programmingc.datasource.remote.mapper.toDomain
import com.example.programmingc.datasource.remote.mapper.toDto
import com.example.programmingc.domain.model.Credential
import com.example.programmingc.domain.model.User
import javax.inject.Inject

class NetworkDaoService @Inject constructor(private val networkDao: NetworkDao): INetworkDaoService {
    override suspend fun authenticate(credential: Credential): User? {
        return networkDao.authenticate(credential.toDto())?.toDomain()
    }
}