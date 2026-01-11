package com.example.programmingc.domain.repo

interface IHintsRepository {
    fun getAvailableHints(): Int
    fun useHint(): Boolean
    fun resetIfNewDay()
    fun getLastResetDate(): Long
    fun getTodayUsedHints(): Int
}