package com.example.programmingc.datasource.remote.dao

import com.example.programmingc.datasource.remote.api.INetworkApi
import com.example.programmingc.datasource.remote.model.CredentialDto
import com.example.programmingc.datasource.remote.model.UserDto
import java.io.IOException
import javax.inject.Inject

class NetworkDao @Inject constructor(private val api: INetworkApi) {
    suspend fun authenticate(credential: CredentialDto): UserDto? {
        return try {
            val request = api.authenticate(credential)

            if (request.isSuccessfull){
                request.body
            }
            else{
                null
            }
        } catch (e: IOException){
            null
        }
    }
}