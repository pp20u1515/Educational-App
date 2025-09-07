package com.example.programmingc.domain.model

sealed class Practice {
    data class CodePractice(
        val id: String,
        val question: String,
        val testCases: List<TestCase>,
        val hint: String? = null
    ) : Practice()

    data class QuizPractice(
        val id: String,
        val question: List<QuizQuestion>
    ): Practice()
}
