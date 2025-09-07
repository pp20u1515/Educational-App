package com.example.programmingc.domain.usecase

import com.example.programmingc.data.repository.LessonRepository
import javax.inject.Inject

class CompleteLessonUseCase @Inject constructor(
    private val lessonRepository: LessonRepository
) {
    suspend operator fun invoke(lessonId: String){
        lessonRepository.markLessonCompleted(lessonId)
    }
}