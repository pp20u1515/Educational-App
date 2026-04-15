package com.example.programmingc.data.source.remote.dao


import com.example.programmingc.data.source.remote.mapper.toDomain
import com.example.programmingc.data.source.remote.model.DiamondsDto
import com.example.programmingc.data.source.remote.model.LiveDto
import com.example.programmingc.domain.model.Diamonds
import com.example.programmingc.domain.model.Live
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseStorageDao @Inject constructor(
    private var remoteDatabase: FirebaseDatabase
){
    suspend fun register(userId: String): Boolean{
        return try {
            val live = remoteDatabase.getReference("Live/$userId")
            val diamonds = remoteDatabase.getReference("Diamonds/$userId")

            live.setValue(LiveDto(userId = userId)).await()
            diamonds.setValue(DiamondsDto(userId = userId)).await()

            true
        } catch (e: Exception){
            false
        }
    }

    suspend fun getLivesByUserId(userId: String): Live?{
        return try {
            val result = remoteDatabase.getReference("Live/$userId").get().await()

            result.getValue(LiveDto::class.java)?.toDomain()
        }catch (e: Exception){
            e.printStackTrace()
            null
        }
    }

    suspend fun getDiamondsByUserId(userId: String): Diamonds?{
        return try {
            val result = remoteDatabase.getReference("Diamonds/$userId").get().await()

            result.getValue(DiamondsDto::class.java)?.toDomain()
        }catch (e: Exception){
            e.printStackTrace()
            null
        }
    }
}