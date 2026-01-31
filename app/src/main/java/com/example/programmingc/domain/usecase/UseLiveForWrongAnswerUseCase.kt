package com.example.programmingc.domain.usecase

import com.example.programmingc.data.repository.LivesRepository
import com.example.programmingc.domain.repo.ILivesRepository
import javax.inject.Inject

class UseLiveForWrongAnswerUseCase @Inject constructor(
    private val liveRepository: ILivesRepository
) {
    suspend operator fun invoke(): LivesRepository.UseLiveResult{
        return liveRepository.useLive()
    }
}