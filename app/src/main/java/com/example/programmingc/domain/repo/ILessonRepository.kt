package com.example.programmingc.domain.repo

import com.example.programmingc.domain.model.Lesson
import com.example.programmingc.domain.model.LessonWindow
import com.example.programmingc.domain.model.Practice

interface ILessonRepository {
    suspend fun getLessonById(id: String): Lesson
    suspend fun getPracticeByLessonId(lessonId: String): Practice?
    suspend fun markLessonCompleted(lessonId: String)

    suspend fun showLessons(): List<LessonWindow>
}