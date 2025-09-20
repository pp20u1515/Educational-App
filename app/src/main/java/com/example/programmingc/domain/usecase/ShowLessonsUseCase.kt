package com.example.programmingc.domain.usecase

import com.example.programmingc.domain.model.LessonWindow
import com.example.programmingc.domain.repo.ILessonRepository
import javax.inject.Inject

class ShowLessonsUseCase @Inject constructor(
    private val lessonRepository: ILessonRepository
) {
    suspend operator fun invoke(): List<LessonWindow>{
        return lessonRepository.showLessons()
    }
}