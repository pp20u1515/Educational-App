package com.example.programmingc.domain.usecase

import com.example.programmingc.data.repository.LessonRepository
import com.example.programmingc.domain.repo.ILessonRepository
import javax.inject.Inject

class CompleteLessonUseCase @Inject constructor(
    private val lessonRepository: ILessonRepository
) {
    suspend operator fun invoke(lessonId: String){
        lessonRepository.markLessonCompleted(lessonId)
    }
}