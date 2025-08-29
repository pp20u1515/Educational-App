package com.example.programmingc.datasource.remote.mapper

import com.example.programmingc.datasource.remote.model.CredentialDto
import com.example.programmingc.datasource.remote.model.UserDto
import com.example.programmingc.domain.model.Credential
import com.example.programmingc.domain.model.User
import com.google.firebase.auth.FirebaseUser

// region domain->dto
fun User.toDto(): UserDto = UserDto(
    id = id,
    password = password,
    email = email
)

fun Credential.toDto(): CredentialDto = CredentialDto(
    id = id,
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
    id = id,
    email = email,
    password = password
)

fun FirebaseUser.toDomain(): User = User(
    id = uid,
    email = email ?: "",
    password = displayName ?: ""
)
// endregion