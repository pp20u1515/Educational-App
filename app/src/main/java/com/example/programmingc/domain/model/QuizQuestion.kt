package com.example.programmingc.domain.model

data class QuizQuestion(
    val id: String,
    val question: String,
    val options: List<String>,
    val correctAnswers: List<String>, // Для поддержки множественных ответов
    val questionType: QuestionType, // Тип вопроса
)

enum class QuestionType {
    SINGLE_CHOICE,  // Один правильный ответ
    MULTIPLE_CHOICE, // Несколько правильных ответов
    INPUT_CHOICE // Ответ с вводом
}
