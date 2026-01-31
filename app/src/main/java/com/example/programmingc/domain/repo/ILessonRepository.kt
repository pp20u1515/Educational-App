package com.example.programmingc.domain.repo

import com.example.programmingc.domain.model.Lesson
import com.example.programmingc.domain.model.LessonWindow

interface ILessonRepository {
    suspend fun getLessonById(id: String): Lesson

    suspend fun showLessons(): List<LessonWindow>
}