package com.example.databasedependencies.db

import androidx.room.Database
import com.example.databasedependencies.dao.CredentialDao
import com.example.databasedependencies.dao.UserDao
import com.example.databasedependencies.entity.CredentialEntity
import com.example.databasedependencies.entity.UserEntity
import androidx.room.RoomDatabase
import com.example.databasedependencies.dao.DailyHintStatusDao
import com.example.databasedependencies.dao.DiamondsDao
import com.example.databasedependencies.dao.HintDao
import com.example.databasedependencies.entity.DailyHintStatusEntity
import com.example.databasedependencies.entity.DiamondsEntity
import com.example.databasedependencies.entity.HintEntity

@Database(
    entities = [
        UserEntity::class,
        CredentialEntity::class,
        HintEntity::class,
        DiamondsEntity::class,
        DailyHintStatusEntity::class],
    version = 1,
    exportSchema = true
)
abstract class Database: RoomDatabase() {
    abstract fun getUserDao(): UserDao
    abstract fun getCredentialDao(): CredentialDao

    abstract fun getHintDao(): HintDao

    abstract fun getDiamondsDao(): DiamondsDao

    abstract fun getDailyHintStatusDao(): DailyHintStatusDao
}