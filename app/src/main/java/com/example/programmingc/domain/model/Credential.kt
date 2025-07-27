package com.example.programmingc.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "Credential")
data class Credential (
    @ColumnInfo(name = "email")
    val email: String,
    @ColumnInfo(name = "password")
    val password: String,
)