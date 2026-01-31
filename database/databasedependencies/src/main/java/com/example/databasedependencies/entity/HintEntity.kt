package com.example.databasedependencies.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Hints",
    foreignKeys = [ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class HintEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val userId: String,
    val isUsed: Boolean = false,
    val lastResetDate: Long = System.currentTimeMillis(),
    val dailyHints: Int = 5,
    val dailyLimit: Int = 5
)
