package com.example.databasedependencies.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Diamonds",
    foreignKeys = [ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class DiamondsEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val availableDiamonds: Int

)
