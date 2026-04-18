package com.example.programmingc.data.repository

import com.example.programmingc.data.source.local.service.DiamondsDaoService
import com.example.programmingc.data.source.local.service.LivesDaoService
import com.example.programmingc.data.source.local.service.UserDaoService
import com.example.programmingc.data.source.remote.service.INetworkDataSource
import com.example.programmingc.data.source.remote.mapper.toDomain
import com.example.programmingc.data.source.remote.mapper.toDto
import com.example.programmingc.domain.model.Credential
import com.example.programmingc.domain.model.Diamonds
import com.example.programmingc.domain.model.Live
import com.example.programmingc.domain.repo.IAuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val networkDataSource: INetworkDataSource,
    private val userDaoService: UserDaoService,
    private val livesDaoService: LivesDaoService,
    private val diamondsDaoService: DiamondsDaoService
): IAuthRepository {
    override suspend fun isUserAuthenticated(): Boolean {
        val firebaseUser = firebaseAuth.currentUser
        var rc = false

        if (firebaseUser != null){
            rc = true
        }
        return rc
    }

    // TODO need to check the result if some operations fail
    override suspend fun authenticate(credential: Credential): Result<Boolean> {
        return try {
            val localUser = userDaoService.readByEmail(credential.email)

            if (localUser == null){
                val remoteUser = networkDataSource.authenticate(credential.toDto())

                if (remoteUser.isFailure){
                    return Result.failure(remoteUser.exceptionOrNull() ?: Exception("Authentication failed!"))
                }
                else{
                    val userDto = remoteUser.getOrNull() ?: return Result.failure(Exception("User data is null!"))
                    val user = userDto.toDomain()

                    userDaoService.insert(user)
                    userDaoService.updateActiveUser(user.id)

                    val livesInfo = networkDataSource.getLivesByUserId(user.id)
                    val diamondsInfo = networkDataSource.getDiamondsByUserId(user.id)

                    if (livesInfo.isSuccess) {
                        livesInfo.getOrNull()?.let { liveDto ->
                            livesDaoService.insert(
                                Live(
                                    id = liveDto.id,
                                    userId = liveDto.userId,
                                    isUsed = liveDto.isUsed,
                                    lastResetDate = liveDto.lastResetDate,
                                    dailyHints = liveDto.dailyHints,
                                    dailyLimit = liveDto.dailyLimit
                                )
                            )
                        }
                    }

                    if (diamondsInfo.isSuccess) {
                        diamondsInfo.getOrNull()?.let { diamondsDto ->
                            diamondsDaoService.insert(
                                Diamonds(
                                    id = diamondsDto.id,
                                    userId = diamondsDto.userId,
                                    availableDiamonds = diamondsDto.availableDiamonds
                                )
                            )
                        }
                    }
                }
            }
            else{
                userDaoService.updateActiveUser(localUser.id)
            }
            Result.success(true)
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun createAcc(credential: Credential): Result<FirebaseUser> {
        return try {
            val user = networkDataSource.registerUser(credential.toDto())

            if (user.isSuccess){
                val firebaseUser = user.getOrNull() ?: return Result.failure(Exception("User is null after registration"))
                val domainUser = firebaseUser.toDomain()
                val rc = networkDataSource.registerUserResources(userId = domainUser.id)

                if (rc.isSuccess){
                    userDaoService.insert(domainUser)
                    livesDaoService.insert(Live(userId = domainUser.id))
                    diamondsDaoService.insert(Diamonds(userId = domainUser.id))
                    Result.success(firebaseUser)
                }else{
                    //networkDaoService.deleteUser()
                    Result.failure(Exception("Failed to initialize user resources"))
                }
            }
            else{
                Result.failure(user.exceptionOrNull() ?: Exception("Registration failed!"))
            }
        }catch (e: Exception){
            Result.failure(e)
        }

    }

    override suspend fun resetPassword(email: String): Result<Boolean> {
        return networkDataSource.resetPassword(email)
    }

    override suspend fun signOut(): Boolean {
        return try {
            val currentUser = userDaoService.getCurrentUser()

            if (currentUser != null){
                userDaoService.updateNotActiveUser(currentUser.id)
                firebaseAuth.signOut()
                true
            } else {
                false
            }
        }
        catch (e: Exception){
            false
        }
    }
}