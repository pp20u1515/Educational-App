package com.example.programmingc.domain.model

data class Hint(
    val id: String,
    val userId: String,
    val text: String,
    val isUsed: Boolean = false
)