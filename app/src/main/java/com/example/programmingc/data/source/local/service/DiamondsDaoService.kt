package com.example.programmingc.data.source.local.service

import com.example.databasedependencies.db.Database
import com.example.programmingc.data.source.local.mapper.toEntity
import com.example.programmingc.domain.model.Diamonds
import javax.inject.Inject

class DiamondsDaoService @Inject constructor(database: Database) {
    private val diamondsDao = database.getDiamondsDao()

    suspend fun insert(diamonds: Diamonds){
        diamondsDao.insert(diamonds.toEntity())
    }

    suspend fun update(diamonds: Diamonds){
        diamondsDao.update(diamonds.toEntity())
    }

    suspend fun delete(diamonds: Diamonds){
        diamondsDao.delete(diamonds.toEntity())
    }
}