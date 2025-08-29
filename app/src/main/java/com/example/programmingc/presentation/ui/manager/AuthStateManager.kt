package com.example.programmingc.presentation.ui.manager

import com.example.programmingc.presentation.ui.auth.AuthViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class AuthStateManager @Inject constructor() {
    private val _authState = MutableStateFlow<AuthViewModel.AuthState>(AuthViewModel.AuthState.Loading)
    val authState: StateFlow<AuthViewModel.AuthState> = _authState.asStateFlow()

    fun setAuthState(state: AuthViewModel.AuthState) {
        _authState.value = state
    }
}