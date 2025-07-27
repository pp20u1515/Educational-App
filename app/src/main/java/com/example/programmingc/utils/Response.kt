package com.example.programmingc.utils

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "Response")
data class Response<T>(
    @ColumnInfo(name = "body")
    val body: T,
    @ColumnInfo("isSuccessfull")
    val isSuccessfull: Boolean
)
