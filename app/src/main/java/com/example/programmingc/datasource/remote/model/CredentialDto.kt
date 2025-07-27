package com.example.programmingc.datasource.remote.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "CredentialDto")
data class CredentialDto(
    @ColumnInfo(name = "email")
    val email: String,
    @ColumnInfo(name = "password")
    val password: String
)