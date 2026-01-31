package com.example.programmingc.domain.usecase

import com.example.programmingc.domain.repo.ILivesRepository
import javax.inject.Inject

class ResetIfNewDayUseCase @Inject constructor(
    private val hintRepository: ILivesRepository
) {
    suspend operator fun invoke(){
        hintRepository.resetIfNewDay()
    }
}