package com.example.databasedependencies.db

import androidx.room.Database
import com.example.databasedependencies.dao.CredentialDao
import com.example.databasedependencies.dao.UserDao
import com.example.databasedependencies.entity.CredentialEntity
import com.example.databasedependencies.entity.UserEntity
import androidx.room.RoomDatabase

@Database(
    entities = [UserEntity::class, CredentialEntity::class],
    version = 1,
    exportSchema = true
)
abstract class Database: RoomDatabase() {
    abstract fun getUserDao(): UserDao
    abstract fun getCredentialDao(): CredentialDao
}