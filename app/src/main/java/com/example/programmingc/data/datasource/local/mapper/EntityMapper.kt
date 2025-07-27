package com.example.programmingc.data.datasource.local.mapper

import com.example.databasedependencies.entity.CredentialEntity
import com.example.databasedependencies.entity.UserEntity
import com.example.programmingc.domain.model.Credential
import com.example.programmingc.domain.model.User

// region entity -> domain
fun CredentialEntity.toDomain(): Credential = Credential(
    email = email,
    password = password
)

fun UserEntity.toDomain(): User = User(
    id = id,
    email = email,
    password = password
)
// endregion

// region domain -> entity
fun Credential.toEntity(): CredentialEntity = CredentialEntity(
    email = email,
    password = password
)

fun User.toEntity(): UserEntity = UserEntity(
    id = id,
    email = email,
    password = password
)
// endregion