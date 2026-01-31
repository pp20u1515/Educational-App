package com.example.programmingc.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.programmingc.domain.usecase.CheckAuthStateUseCase
import com.example.programmingc.domain.usecase.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val checkAuthStateUseCase: CheckAuthStateUseCase,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
    private val _navigationEvents = MutableSharedFlow<MenuNavigationEvent>()
    val navigationEvent = _navigationEvents.asSharedFlow()
    private val _menuBarVisible = MutableStateFlow(true)
    val menuBarVisible: StateFlow<Boolean> = _menuBarVisible

    fun checkAuthState() {
        viewModelScope.launch {
            val result = checkAuthStateUseCase.invoke()

            if (result) {
                _authState.value = AuthState.Authenticated
            } else {
                _authState.value = AuthState.Unauthenticated
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            val result = signOutUseCase()

            if (result) {
                _authState.value = AuthState.Unauthenticated
            } else {
                _authState.value = AuthState.Error( "Logout failed")
            }
        }
    }

    fun showMenu(){
        _menuBarVisible.value = true
    }

    fun hideMenu(){
        _menuBarVisible.value = false
    }

    fun navigateTo(event: MenuNavigationEvent){
        viewModelScope.launch {
            _navigationEvents.emit(event)
        }
    }

    sealed class AuthState {
        object Loading : AuthState()
        object Authenticated : AuthState()
        object Unauthenticated : AuthState()
        data class Error(val message: String) : AuthState()
    }

    sealed class MenuNavigationEvent {
        object ToDiamonds: MenuNavigationEvent()
        object ToLives: MenuNavigationEvent()
    }
}