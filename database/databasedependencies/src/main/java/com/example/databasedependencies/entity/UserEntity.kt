package com.example.databasedependencies.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.util.UUID

@Entity(tableName = "UserEntity")
data class UserEntity (
    @ColumnInfo("id")
    val id: UUID,
    @ColumnInfo("email")
    val email: String,
    @ColumnInfo("password")
    val password: String
)