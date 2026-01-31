package com.example.programmingc.domain.usecase

import com.example.programmingc.domain.repo.ILivesRepository
import javax.inject.Inject

class LivesUpdateUseCase @Inject constructor(
    private val livesRepository: ILivesRepository
) {
    suspend operator fun invoke(){
        livesRepository.initLives()
    }
}