package com.example.programmingc.domain.usecase

import com.example.programmingc.domain.model.Lesson
import com.example.programmingc.domain.repo.ILessonRepository
import javax.inject.Inject

class GetLessonUseCase @Inject constructor(
    private val lessonRepository: ILessonRepository
) {
    suspend operator fun invoke(lessonId: String): Lesson{
        return lessonRepository.getLessonById(lessonId)
    }
}