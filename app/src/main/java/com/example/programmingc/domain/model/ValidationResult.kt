package com.example.programmingc.domain.model

data class ValidationResult(
    val isCorrect: Boolean,
    val message: String,
    val score: Int = if (isCorrect) 5 else 0
)
