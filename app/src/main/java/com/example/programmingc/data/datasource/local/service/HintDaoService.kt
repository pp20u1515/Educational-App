package com.example.programmingc.data.datasource.local.service

import com.example.databasedependencies.db.Database
import com.example.programmingc.data.datasource.local.mapper.toDomain
import com.example.programmingc.data.datasource.local.mapper.toEntity
import com.example.programmingc.domain.model.Hint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HintDaoService @Inject constructor(database: Database) {
    private val hintDao = database.getHintDao()

    suspend fun insert(hint: Hint){
        hintDao.insert(hint.toEntity())
    }

    fun readAll(): Flow<List<Hint>>{
        return hintDao.readAll().map { hints -> hints.map { it.toDomain() } }
    }

    suspend fun update(hint: Hint){
        hintDao.update(hint.toEntity())
    }

    suspend fun delete(hint: Hint){
        hintDao.delete(hint.toEntity())
    }
}