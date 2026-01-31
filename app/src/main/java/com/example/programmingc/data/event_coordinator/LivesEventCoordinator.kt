package com.example.programmingc.data.event_coordinator

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LivesEventCoordinator @Inject constructor() {
    private val _liveEvents = MutableSharedFlow<LiveEvent>(
        replay = 1,
        extraBufferCapacity = 64
    )
    val liveEvents = _liveEvents.asSharedFlow()

    suspend fun emit(event: LiveEvent) {
        _liveEvents.emit(event)
    }

    sealed class LiveEvent {
        object LiveUsed : LiveEvent()
        data class LivesUpdated(val count: Int) : LiveEvent()
        object RefreshRequested : LiveEvent()
    }
}