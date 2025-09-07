package com.example.programmingc.domain.model

data class QuizQuestion(
    val id: String,
    val question: String,
    val correctAnswer: Int,
    val explanation: String? = null
)
