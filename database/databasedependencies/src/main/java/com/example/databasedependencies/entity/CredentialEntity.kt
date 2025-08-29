package com.example.databasedependencies.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Credentials")
data class CredentialEntity (
    @PrimaryKey
    val id: Long = 0L,
    val email: String,
    val password: String
)