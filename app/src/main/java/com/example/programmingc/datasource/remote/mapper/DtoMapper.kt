package com.example.programmingc.datasource.remote.mapper

import com.example.programmingc.datasource.remote.model.CredentialDto
import com.example.programmingc.datasource.remote.model.UserDto
import com.example.programmingc.domain.model.Credential
import com.example.programmingc.domain.model.User

// region domain->dto
fun User.toDto(): UserDto = UserDto(
    id = id,
    password = password,
    email = email
)

fun Credential.toDto(): CredentialDto = CredentialDto(
    email = email,
    password = password
)
// endregion

// region dto->domain
fun UserDto.toDomain(): User = User(
    id = id,
    password = password,
    email = email
)

fun CredentialDto.toDomain(): Credential = Credential(
    email = email,
    password = password
)
// endregion