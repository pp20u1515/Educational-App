package com.example.programmingc.data.datasource.local.mapper

import com.example.databasedependencies.entity.CredentialEntity
import com.example.databasedependencies.entity.DiamondsEntity
import com.example.databasedependencies.entity.HintEntity
import com.example.databasedependencies.entity.UserEntity
import com.example.programmingc.domain.model.Credential
import com.example.programmingc.domain.model.Diamonds
import com.example.programmingc.domain.model.Live
import com.example.programmingc.domain.model.User

// region entity -> domain
fun CredentialEntity.toDomain(): Credential = Credential(
    id = id,
    email = email,
    password = password
)

fun UserEntity.toDomain(): User = User(
    id = id,
    email = email,
    password = password,
    registrationDate = registrationDate
)

fun HintEntity.toDomain(): Live = Live(
    id = id,
    userId = userId,
    isUsed = isUsed,
    lastResetDate = lastResetDate,
    dailyHints = dailyHints,
    dailyLimit = dailyLimit
)

fun DiamondsEntity.toDomain(): Diamonds = Diamonds(
    id = id,
    userId = userId,
    availableDiamonds = availableDiamonds
)
// endregion

// region domain -> entity
fun Credential.toEntity(): CredentialEntity = CredentialEntity(
    id = id,
    email = email,
    password = password,
)

fun User.toEntity(): UserEntity = UserEntity(
    id = id,
    email = email,
    password = password,
    registrationDate = registrationDate
)

fun Live.toEntity(): HintEntity = HintEntity(
    id = id,
    userId = userId,
    isUsed = isUsed,
    lastResetDate = lastResetDate,
    dailyHints = dailyHints,
    dailyLimit = dailyLimit
)

fun Diamonds.toEntity(): DiamondsEntity = DiamondsEntity(
    id = id,
    userId = userId,
    availableDiamonds = availableDiamonds
)
// endregion