package com.example.programmingc.data.repository

import com.example.programmingc.data.source.local.service.DiamondsDaoService
import com.example.programmingc.data.source.local.service.LivesDaoService
import com.example.programmingc.data.source.local.service.UserDaoService
import com.example.programmingc.data.source.remote.service.INetworkDaoService
import com.example.programmingc.data.source.remote.mapper.toDomain
import com.example.programmingc.domain.model.Credential
import com.example.programmingc.domain.model.Diamonds
import com.example.programmingc.domain.model.Live
import com.example.programmingc.domain.repo.IAuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val networkDaoService: INetworkDaoService,
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
    override suspend fun authenticate(credential: Credential): Boolean {
        var rc = true
        var user = userDaoService.readByEmail(credential.email)

        if (user == null){
            user = networkDaoService.authenticate(credential)

            if (user == null){
                rc = false
            }
            else{
                userDaoService.insert(user)
                userDaoService.updateActiveUser(user.id)

                val livesInfo = networkDaoService.getLivesByUserId(user.id)
                val diamondsInfo = networkDaoService.getDiamondsByUserId(user.id)

                livesInfo?.let {
                    livesDaoService.insert(Live(
                        id = it.id,
                        userId = it.userId,
                        isUsed = it.isUsed,
                        lastResetDate = it.lastResetDate,
                        dailyHints = it.dailyHints,
                        dailyLimit = it.dailyLimit))
                }

                diamondsInfo?.let {
                    diamondsDaoService.insert(Diamonds(
                        id = it.id,
                        userId = it.userId,
                        availableDiamonds = it.availableDiamonds
                    ))
                }
            }
        }
        else{
            userDaoService.updateActiveUser(user.id)
        }
        return rc
    }

    override suspend fun createAcc(credential: Credential): FirebaseUser? {
        val user = networkDaoService.register(credential)

        if (user != null){
            val domainUser = user.toDomain()
            try {
                val rc = networkDaoService.initializeUserResources(userId = domainUser.id)

                if (rc){
                    userDaoService.insert(domainUser)
                    livesDaoService.insert(Live(userId = domainUser.id))
                    diamondsDaoService.insert(Diamonds(userId = domainUser.id))
                }else{
                    //networkDaoService.deleteUser()
                    return null
                }
            } catch (e: Exception){
                //networkDaoService.deleteUser()
                throw e
            }
        }
        else{
            return null
        }
        return user
    }

    override suspend fun resetPassword(email: String): Boolean {
        return networkDaoService.resetPassword(email)
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