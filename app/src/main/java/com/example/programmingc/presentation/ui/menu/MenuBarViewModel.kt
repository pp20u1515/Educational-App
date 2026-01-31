package com.example.programmingc.presentation.ui.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.programmingc.data.event_coordinator.LivesEventCoordinator
import com.example.programmingc.domain.usecase.LivesUpdateUseCase
import com.example.programmingc.domain.usecase.ResetIfNewDayUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuBarViewModel @Inject constructor(
    private val livesUpdateUseCase: LivesUpdateUseCase,
    private val livesEventCoordinator: LivesEventCoordinator,
    private val resetIfNewDayUseCase: ResetIfNewDayUseCase
): ViewModel() {
    private val _liveCount = MutableStateFlow(0)
    val liveCount: StateFlow<Int> = _liveCount

    init {
        viewModelScope.launch {
            resetIfNewDayUseCase.invoke()
            livesUpdateUseCase.invoke()

            livesEventCoordinator.liveEvents.collect { event ->
                when (event) {
                    is LivesEventCoordinator.LiveEvent.LivesUpdated -> {
                        _liveCount.value = event.count
                    }
                    LivesEventCoordinator.LiveEvent.RefreshRequested -> {
                        livesUpdateUseCase.invoke()
                    }
                    else -> Unit
                }
            }
        }
    }
}