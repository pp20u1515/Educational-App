package com.example.programmingc.data.datasource.local.service

import com.example.databasedependencies.db.Database
import com.example.programmingc.data.datasource.local.mapper.toEntity
import com.example.programmingc.domain.model.Live
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LivesDaoService @Inject constructor(database: Database) {
    private val hintDao = database.getHintDao()

    suspend fun insert(live: Live){
        hintDao.insert(live.toEntity())
    }

    suspend fun delete(live: Live){
        hintDao.delete(live.toEntity())
    }

    suspend fun useDailyHint(userId: String): Int{
        return hintDao.useDailyHint(userId)
    }

    suspend fun getAvailableHints(userId: String): Int{
        return hintDao.getAvailableHints(userId)
    }

    suspend fun getLastResetDate(userId: String): Long?{
        return hintDao.getLastResetDate(userId)
    }

    suspend fun needsReset(userId: String): Int{
        return hintDao.needsReset(userId)
    }

    suspend fun resetToDailyLimit(userId: String, currentTime: Long): Int{
        return hintDao.resetToDailyLimit(userId, currentTime)
    }

    suspend fun observeAvailableHints(userId: String): Flow<Int>{
        return hintDao.observeAvailableHints(userId)
    }
}