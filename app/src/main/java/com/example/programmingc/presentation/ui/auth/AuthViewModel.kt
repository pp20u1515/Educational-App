package com.example.programmingc.presentation.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.programmingc.domain.model.Credential
import com.example.programmingc.domain.usecase.AuthenticateUseCase
import com.example.programmingc.domain.usecase.ValidateCredentialsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authenticateUseCase: AuthenticateUseCase,
    private val validateCredentialUseCase: ValidateCredentialsUseCase
): ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun onLogInClick(email: String, password: String){
        viewModelScope.launch {
            _authState.value = AuthState.Idle

            val validResult = validateCredentialUseCase.invoke(email, password)

            when (validResult){
                is ValidateCredentialsUseCase.ValidationResult.Error -> {
                    _authState.value = AuthState.ValidationError(
                        message = validResult.message
                    )
                }
                is ValidateCredentialsUseCase.ValidationResult.Success -> {
                    val authResult = authenticateUseCase.invoke(Credential(
                        email = email,
                        password = password))

                    if (authResult) {
                        _authState.value = AuthState.Success
                    }
                    else{
                        _authState.value = AuthState.Error("Error!")
                    }
                }
            }
        }
    }

    sealed class AuthState {
        object Idle: AuthState()
        object Success : AuthState()
        data class ValidationError(val message: String) : AuthState()
        data class Error(val message: String): AuthState() // Error with authentication/net
    }
}