package com.example.programmingc.domain.model

data class LessonWindow(
    val id: String,
    val number: Int,
    val title: String,
    val text: String,
    val imageResIds: List<Int>
)
