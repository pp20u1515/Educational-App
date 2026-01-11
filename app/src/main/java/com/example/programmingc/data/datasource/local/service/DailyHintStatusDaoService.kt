package com.example.programmingc.data.datasource.local.service

import com.example.databasedependencies.db.Database
import com.example.programmingc.data.datasource.local.mapper.toDomain
import com.example.programmingc.data.datasource.local.mapper.toEntity
import com.example.programmingc.domain.model.DailyHintStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DailyHintStatusDaoService @Inject constructor(database: Database){
    private val dailyHintStatusDao = database.getDailyHintStatusDao()

    suspend fun insert(dailyHintStatus: DailyHintStatus){
        dailyHintStatusDao.insert(dailyHintStatus.toEntity())
    }

    fun readAll(): Flow<List<DailyHintStatus>>{
        return dailyHintStatusDao.readAll().map { status -> status.map { it.toDomain() } }
    }

    suspend fun update(dailyHintStatus: DailyHintStatus){
        dailyHintStatusDao.update(dailyHintStatus.toEntity())
    }

    suspend fun delete(dailyHintStatus: DailyHintStatus){
        dailyHintStatusDao.delete(dailyHintStatus.toEntity())
    }
}
