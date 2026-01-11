package com.example.programmingc.domain.usecase

import com.example.programmingc.domain.repo.IHintsRepository
import javax.inject.Inject

class GetDailyHintsUseCase @Inject constructor(
    private val hintsRepository: IHintsRepository
) {
    fun invoke(): Int {
        return hintsRepository.getAvailableHints()
    }
}