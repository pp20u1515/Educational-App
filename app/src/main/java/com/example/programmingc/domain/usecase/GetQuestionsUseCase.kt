package com.example.programmingc.domain.usecase

import com.example.programmingc.domain.model.QuizQuestion
import com.example.programmingc.domain.repo.IQuestionsRepository
import javax.inject.Inject

class GetQuestionsUseCase @Inject constructor(
    private val questionsRepository: IQuestionsRepository
) {
    suspend operator fun invoke(lectureId: Int): List<QuizQuestion> {
        val questions = questionsRepository.getQuestionsForLecture(lectureId)

        if (questions.isNullOrEmpty()) {
            throw Exception("No questions found for lecture $lectureId")
        }
        return questions
    }
}