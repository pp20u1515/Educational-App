package com.example.databasedependencies.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "CredentialEntity")
data class CredentialEntity (
    @ColumnInfo("email")
    val email: String,
    @ColumnInfo("password")
    val password: String
)