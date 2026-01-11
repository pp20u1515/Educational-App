package com.example.programmingc.domain.model

data class DailyHintStatus(
    val id: String,
    val userId: String,
    val available: Int,
    val usedToday: Int,
    val dailyLimit: Int,
    val nextResetTime: Long
)
