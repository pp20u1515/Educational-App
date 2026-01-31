package com.example.databasedependencies.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Users")
data class UserEntity (
    @PrimaryKey
    val id: String,
    val email: String,
    val password: String,
    val registrationDate: Long = System.currentTimeMillis(),
    val isCurrent: Boolean = false
)