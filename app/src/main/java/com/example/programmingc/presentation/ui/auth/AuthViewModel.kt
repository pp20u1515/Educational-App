package com.example.programmingc.presentation.ui.auth

import androidx.lifecycle.ViewModel
import com.example.programmingc.domain.model.Credential
import com.example.programmingc.domain.usecase.AuthenticateUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val authUseCase: AuthenticateUseCase
): ViewModel() {
    sealed class AuthState{
        object Loading: AuthState()
        object Authenticated: AuthState()
        object Unauthenticated: AuthState()
        data class Error(val message: String): AuthState()
    }
    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun checkAuthState(){
        _authState.value = if (firebaseAuth.currentUser != null){
            AuthState.Authenticated
        } else{
            AuthState.Unauthenticated
        }
    }

    suspend fun authenticate(email: String, password: String){
        _authState.value = AuthState.Loading
        try {
            authUseCase(Credential(email = email, password = password))
            _authState.value = AuthState.Authenticated
        } catch (e: Exception){
            _authState.value = AuthState.Error(e.message ?: "Authentication failed")
        }
    }

    fun signOut(){
        try {
            firebaseAuth.signOut()
            _authState.value = AuthState.Unauthenticated
        } catch (e: Exception){
            _authState.value = AuthState.Error("Logout failed: ${e.message}")
        }
    }
}