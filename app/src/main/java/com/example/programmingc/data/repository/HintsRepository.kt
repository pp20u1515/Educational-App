package com.example.programmingc.data.repository

import com.example.programmingc.domain.repo.IHintsRepository
import javax.inject.Inject

class HintsRepository @Inject constructor(): IHintsRepository {
    companion object{
        const val DAILY_HINTS_LIMIT = 5
    }

    override fun getAvailableHints(): Int {
        resetIfNewDay()
        return DAILY_HINTS_LIMIT - getTodayUsedHints()
    }

    override fun resetIfNewDay() {

    }
}