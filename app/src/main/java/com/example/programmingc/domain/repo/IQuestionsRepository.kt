package com.example.programmingc.domain.repo

import com.example.programmingc.domain.model.QuizQuestion

interface IQuestionsRepository {
    suspend fun getQuestionsForLecture(lectureId: Int): List<QuizQuestion>?
    suspend fun getAvailableLectureIds(): List<Int>
}