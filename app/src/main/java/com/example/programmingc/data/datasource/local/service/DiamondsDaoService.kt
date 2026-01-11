package com.example.programmingc.data.datasource.local.service

import com.example.databasedependencies.db.Database
import com.example.programmingc.data.datasource.local.mapper.toDomain
import com.example.programmingc.data.datasource.local.mapper.toEntity
import com.example.programmingc.domain.model.Diamonds
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DiamondsDaoService @Inject constructor(database: Database) {
    private val diamondsDao = database.getDiamondsDao()

    suspend fun insert(diamonds: Diamonds){
        diamondsDao.insert(diamonds.toEntity())
    }

    fun readAll(): Flow<List<Diamonds>>{
        return diamondsDao.readAll().map { diamonds -> diamonds.map { it.toDomain() } }
    }

    suspend fun update(diamonds: Diamonds){
        diamondsDao.update(diamonds.toEntity())
    }

    suspend fun delete(diamonds: Diamonds){
        diamondsDao.delete(diamonds.toEntity())
    }
}