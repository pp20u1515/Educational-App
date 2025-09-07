package com.example.programmingc.domain.model

data class Lesson(
    val id: String,
    val title: String? = null,
    val content: String,
    val isCompleted: Boolean = false,
    val practice: Practice? = null
)
