package com.example.programmingc.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.programmingc.datasource.remote.model.CredentialDto
import com.example.programmingc.datasource.remote.model.UserDto
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.util.UUID
/*
class RetrofitInvocactionHandler :InvocationHandler {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun invoke(proxy: Any, method: Method, args: Array<out Any>): Any {
        return when (method.name){
            "authenticate" -> {
                val credentials = (args[0] as? CredentialDto)
                    ?: throw IllegalArgumentException("В метод переданы не верные параметры")
                getAuthResponse(credentials)
            }
            "getUsers" -> getUsers()
            else -> error("Неизвестное имя метода")
        }
    }
    private fun getAuthResponse(credentialDto: CredentialDto): Response<UserDto>{
        return Response(
            body = UserDto(
                id = UUID(0L, 4L),
                password = credentialDto.password,
                email = "user4@gmail.com"
            ),
            isSuccessfull = true
        )
    }
    private fun getUsers(): Response<List<UserDto>>{
        return Response(
            body = listOf(
                UserDto(
                    id = UUID(0L, 1L),
                    password = "first_owner",
                    email = "user1@gmail.com"
                ),
                UserDto(
                    id = UUID(0L, 2L),
                    password = "second_owner",
                    email = "user2@gmail.com"
                ),
                UserDto(
                    id = UUID(0L, 3L),
                    password = "third_owner",
                    email = "user3@gmail.com"
                )
            ),
            isSuccessfull = true
        )
    }
}*/