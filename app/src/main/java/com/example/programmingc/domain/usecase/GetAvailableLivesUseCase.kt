package com.example.programmingc.domain.usecase

import com.example.programmingc.domain.repo.ILivesRepository
import javax.inject.Inject

class GetAvailableLivesUseCase @Inject constructor(
    private val liveRepository: ILivesRepository
) {
    suspend operator fun invoke(): Int {
        return liveRepository.getAvailableLives()
    }
}