package com.example.programmingc.data.source.remote.service

import com.example.programmingc.data.source.remote.mapper.toDto
import com.example.programmingc.data.source.remote.model.CredentialDto
import com.example.programmingc.data.source.remote.model.DiamondsDto
import com.example.programmingc.data.source.remote.model.LiveDto
import com.example.programmingc.data.source.remote.model.UserDto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class NetworkDataSource @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase
): INetworkDataSource {
    override suspend fun authenticate(credential: CredentialDto): Result<UserDto> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(credential.email, credential.password).await()
            val user = result.user ?: return Result.failure(Exception("User not found!"))
            Result.success(user.toDto())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun registerUser(credential: CredentialDto): Result<FirebaseUser>{
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(credential.email, credential.password).await()
            val user = result.user ?: return Result.failure(Exception("User creation error!"))
            Result.success(user)
        }
        catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun resetPassword(email: String): Result<Boolean>{
        return try {
            firebaseAuth.sendPasswordResetEmail(email)
            Result.success(true)
        }
        catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun registerUserResources(userId: String): Result<Boolean>{
        return try {
            val live = firebaseDatabase.getReference("Live/$userId")
            val diamonds = firebaseDatabase.getReference("Diamonds/$userId")

            live.setValue(LiveDto(userId = userId)).await()
            diamonds.setValue(DiamondsDto(userId = userId)).await()

            Result.success(true)
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun getLivesByUserId(userId: String): Result<LiveDto>{
        return try {
            val result = firebaseDatabase.getReference("Live/$userId").get().await()
            val _value = result.getValue(LiveDto::class.java)

            if (_value != null){
                Result.success(_value)
            }
            else{
                Result.failure(Exception("Live data not found for user $userId"))
            }
        }catch (e: Exception){
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun getDiamondsByUserId(userId: String): Result<DiamondsDto>{
        return try {
            val result = firebaseDatabase.getReference("Diamonds/$userId").get().await()
            val _value = result.getValue(DiamondsDto::class.java)

            if (_value != null){
                Result.success(_value)
            }
            else{
                Result.failure(Exception("Diamond data not found for user $userId"))
            }

        }catch (e: Exception){
            e.printStackTrace()
            Result.failure(e)
        }
    }
}