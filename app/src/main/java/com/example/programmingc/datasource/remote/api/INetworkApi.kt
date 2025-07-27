package com.example.programmingc.datasource.remote.api

import com.example.programmingc.datasource.remote.model.CredentialDto
import com.example.programmingc.datasource.remote.model.UserDto
import com.example.programmingc.utils.GET
import com.example.programmingc.utils.Response


interface INetworkApi {
    @GET("api/v2/authenticate")
    suspend fun authenticate(credential: CredentialDto): Response<UserDto>
}