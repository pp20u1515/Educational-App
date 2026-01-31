package com.example.programmingc.domain.repo

import com.example.programmingc.data.repository.LivesRepository

interface ILivesRepository {
    suspend fun getAvailableLives(): Int
    suspend fun useLive(): LivesRepository.UseLiveResult
    suspend fun resetIfNewDay()
    suspend fun initLives()
}