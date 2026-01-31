package com.example.databasedependencies.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Credentials",
    foreignKeys = [ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["id"],
        childColumns = ["id"],
        onDelete = ForeignKey.CASCADE
    )])
data class CredentialEntity (
    @PrimaryKey
    val id: Long = 0L,
    val email: String,
    val password: String
)