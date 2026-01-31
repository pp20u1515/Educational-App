package com.example.programmingc.presentation.ui.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.programmingc.domain.usecase.GetAvailableLivesUseCase
import com.example.programmingc.domain.usecase.GetTimeUntilResetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class LivesViewModel @Inject constructor(
    private val getAvailableLivesUseCase: GetAvailableLivesUseCase,
    private val getTimeUntilResetUseCase: GetTimeUntilResetUseCase
): ViewModel() {
    private val _timeUntilReset = MutableStateFlow("00h:00m")
    val timeUntilReset: StateFlow<String> = _timeUntilReset

    private var timerJob: Job ?= null

    init {
        startCountDownTimer()
    }

    suspend fun checkAvailableLives(): Int{
        return getAvailableLivesUseCase.invoke()
    }

    private fun startCountDownTimer(){
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true){
                val timeMillis = getTimeUntilResetUseCase.invoke()
                val formattedTime = formatTimeForUI(timeMillis)

                _timeUntilReset.value = formattedTime

                delay(1000)
            }
        }
    }

    private fun formatTimeForUI(time: Long): String{
        val hours = TimeUnit.MILLISECONDS.toHours(time)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(time) % 60
        return String.format("%02dh:%02dm", hours, minutes)
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}