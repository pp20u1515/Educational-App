package com.example.programmingc.domain.model

data class Live(
    val id: Long = 0L,
    val userId: String,
    val isUsed: Boolean = false,
    val lastResetDate: Long = System.currentTimeMillis(),
    val dailyHints: Int = 5,
    val dailyLimit: Int = 5
)