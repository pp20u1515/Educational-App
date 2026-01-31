package com.example.programmingc.domain.model

data class User(
    val id: String,
    val email: String,
    val password: String,
    val registrationDate: Long = System.currentTimeMillis(),
    val isCurrent: Boolean = false
)
