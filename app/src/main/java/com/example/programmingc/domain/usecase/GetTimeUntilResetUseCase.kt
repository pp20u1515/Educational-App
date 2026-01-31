package com.example.programmingc.domain.usecase

import com.example.programmingc.utils.TimeCalculator
import javax.inject.Inject

class GetTimeUntilResetUseCase @Inject constructor(
    private val timeCalculator: TimeCalculator
) {
    suspend operator fun invoke(): Long{
        return timeCalculator.calculateTimeUntilMidnight()
    }
}