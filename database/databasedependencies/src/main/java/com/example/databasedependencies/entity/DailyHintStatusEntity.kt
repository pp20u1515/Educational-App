package com.example.databasedependencies.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "DailyHintStatus",
    foreignKeys = [ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class DailyHintStatusEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val available: Int,
    val usedToday: Int,
    val dailyLimit: Int,
    val nextResetTime: Long
)
