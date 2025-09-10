package com.example.programmingc.presentation.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.programmingc.domain.model.Credential
import com.example.programmingc.domain.usecase.AuthenticateUseCase
import com.example.programmingc.domain.usecase.CheckAuthStateUseCase
import com.example.programmingc.domain.usecase.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.Result

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authUseCase: AuthenticateUseCase,
    private val checkAuthStateUseCase: CheckAuthStateUseCase,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {
    // Состояние аутентификации
    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    // Видимость меню
    val _menuBarVisible = MutableLiveData(true)
    val menuBarVisible: LiveData<Boolean> = _menuBarVisible

    // Проверка состояния аутентификации
    fun checkAuthState() {
        viewModelScope.launch {
            val result = checkAuthStateUseCase()
            _authState.value = if (result.isSuccess) {
                if (result.getOrNull() == true)
                    AuthState.Authenticated
                else
                    AuthState.Unauthenticated
            } else {
                AuthState.Error(result.exceptionOrNull()?.message ?: "Auth check failed")
            }
        }
    }

    // Аутентификация
    suspend fun authenticate(email: String, password: String) {
        _authState.value = AuthState.Loading
        authUseCase(Credential(email = email, password = password))
        _authState.value = AuthState.Authenticated
    }

    // Выход
    fun signOut() {
        viewModelScope.launch {
            val result = signOutUseCase()
            _authState.value = if (result.isSuccess) {
                AuthState.Unauthenticated
            } else {
                AuthState.Error(result.exceptionOrNull()?.message ?: "Logout failed")
            }
        }
    }

    fun showMenu(){
        _menuBarVisible.value = true
    }

    fun hideMenu(){
        _menuBarVisible.value = false
    }

    sealed class AuthState {
        object Loading : AuthState()
        object Authenticated : AuthState()
        object Unauthenticated : AuthState()
        data class Error(val message: String) : AuthState()
    }
}