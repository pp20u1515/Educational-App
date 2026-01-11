package com.example.programmingc.data.datasource.local.mapper

import com.example.databasedependencies.entity.CredentialEntity
import com.example.databasedependencies.entity.DailyHintStatusEntity
import com.example.databasedependencies.entity.DiamondsEntity
import com.example.databasedependencies.entity.HintEntity
import com.example.databasedependencies.entity.UserEntity
import com.example.programmingc.domain.model.Credential
import com.example.programmingc.domain.model.DailyHintStatus
import com.example.programmingc.domain.model.Diamonds
import com.example.programmingc.domain.model.Hint
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

fun HintEntity.toDomain(): Hint = Hint(
    id = id,
    userId = userId,
    text = text,
    isUsed = isUsed
)

fun DiamondsEntity.toDomain(): Diamonds = Diamonds(
    id = id,
    userId = userId,
    availableDiamonds = availableDiamonds
)

fun DailyHintStatusEntity.toDomain(): DailyHintStatus = DailyHintStatus(
    id = id,
    userId = userId,
    available = available,
    usedToday = usedToday,
    dailyLimit = dailyLimit,
    nextResetTime = nextResetTime
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

fun Hint.toEntity(): HintEntity = HintEntity(
    id = id,
    userId = userId,
    text = text,
    isUsed = isUsed
)

fun Diamonds.toEntity(): DiamondsEntity = DiamondsEntity(
    id = id,
    userId = userId,
    availableDiamonds = availableDiamonds
)

fun DailyHintStatus.toEntity(): DailyHintStatusEntity = DailyHintStatusEntity(
    id = id,
    userId = userId,
    available = available,
    usedToday = usedToday,
    dailyLimit = dailyLimit,
    nextResetTime = nextResetTime
)
// endregion