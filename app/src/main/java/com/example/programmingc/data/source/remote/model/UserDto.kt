package com.example.programmingc.data.source.remote.model

data class UserDto(
    val id: String,
    val email: String,
    val password: String,
    val registrationDate: Long = System.currentTimeMillis(),
    val isCurrent: Boolean = false
)
