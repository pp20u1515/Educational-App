package com.example.programmingc.data.repository

import com.example.programmingc.data.datasource.local.service.LivesDaoService
import com.example.programmingc.data.datasource.local.service.UserDaoService
import com.example.programmingc.data.event_coordinator.LivesEventCoordinator
import com.example.programmingc.domain.repo.ILivesRepository
import java.util.Calendar
import javax.inject.Inject

class LivesRepository @Inject constructor(
    private val livesDaoService: LivesDaoService,
    private val userDaoService: UserDaoService,
    private val livesEventCoordinator: LivesEventCoordinator
): ILivesRepository {
    override suspend fun useLive(): UseLiveResult {
        val user = userDaoService.getCurrentUser()

        return if (user != null){
            val canUse = livesDaoService.getAvailableHints(user.id)

            if (canUse > 0){
                val result = livesDaoService.useDailyHint(user.id)

                if (result > 0){
                    val remaining = livesDaoService.getAvailableHints(user.id)

                    livesEventCoordinator.emit(LivesEventCoordinator.LiveEvent.LivesUpdated(remaining))
                    UseLiveResult.Success(remainingLives = remaining)
                }
                else{
                    UseLiveResult.Error("Failed to use hint!")
                }
            }
            else{
                UseLiveResult.NoLivesAvailable
            }
        }
        else{
            UseLiveResult.Error("User not authenticated")
        }
    }

    override suspend fun resetIfNewDay() {
        val user = userDaoService.getCurrentUser()

        if (user != null){
            val lastReset = livesDaoService.getLastResetDate(user.id)
            val now = System.currentTimeMillis()

            if (lastReset != null && isNewDay(lastReset, now)){
                livesDaoService.resetToDailyLimit(user.id, now)
                val remaining = livesDaoService.getAvailableHints(user.id)
                livesEventCoordinator.emit(LivesEventCoordinator.LiveEvent.LivesUpdated(remaining))
            }
        }
        else{
            return
        }
    }

    private fun isNewDay(lastReset: Long, now: Long): Boolean{
        val calendar = Calendar.getInstance()

        calendar.timeInMillis = lastReset
        val lastDay = calendar.get(Calendar.DAY_OF_YEAR)
        val lastYear = calendar.get(Calendar.YEAR)

        calendar.timeInMillis = now
        val currentDay = calendar.get(Calendar.DAY_OF_YEAR)
        val currentYear = calendar.get(Calendar.YEAR)

        return currentDay != lastDay || currentYear != lastYear
    }

    override suspend fun getAvailableLives(): Int {
        val user = userDaoService.getCurrentUser()

        return if (user != null){
            livesDaoService.getAvailableHints(user.id)
        }
        else{
            -1
        }
    }

    override suspend fun initLives() {
        val user = userDaoService.getCurrentUser()

        if (user != null) {
            val remaining = livesDaoService.getAvailableHints(user.id)
            livesEventCoordinator.emit(LivesEventCoordinator.LiveEvent.LivesUpdated(remaining))
        }
    }

    sealed class UseLiveResult {
        data class Success(val remainingLives: Int) : UseLiveResult()
        object NoLivesAvailable : UseLiveResult()
        data class Error(val message: String) : UseLiveResult()
    }
}