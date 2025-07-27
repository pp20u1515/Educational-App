package com.example.programmingc.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.util.UUID

@Entity(tableName = "User")
data class User(
    @ColumnInfo(name = "id")
    val id: UUID,
    @ColumnInfo(name = "email")
    val email: String,
    @ColumnInfo(name = "password")
    val password: String,
)
