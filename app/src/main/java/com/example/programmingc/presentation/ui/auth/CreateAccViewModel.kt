package com.example.programmingc.presentation.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.programmingc.domain.model.Credential
import com.example.programmingc.domain.usecase.CreateAccUseCase
import com.example.programmingc.domain.usecase.ValidateCredentialsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateAccViewModel @Inject constructor(
    private val createAccUseCase: CreateAccUseCase,
    private val validateCredentialsUseCase: ValidateCredentialsUseCase
): ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
    fun createAcc(email: String, password: String){
        viewModelScope.launch {
            _authState.value = AuthState.Idle

            val validResult = validateCredentialsUseCase.invoke(email, password)

            when (validResult){
                is ValidateCredentialsUseCase.ValidationResult.Error -> {
                    _authState.value = AuthState.ValidationError(validResult.message)
                }
                is ValidateCredentialsUseCase.ValidationResult.Success -> {
                    val createAccResult = createAccUseCase.invoke(Credential(email = email, password = password))

                    if (createAccResult == null){
                        _authState.value = AuthState.Error("Failed to create account. The email may already be in use.")
                    }
                    else
                    {
                        _authState.value = AuthState.Success
                    }
                }
            }
        }
    }

    sealed class AuthState(){
        object Idle: AuthState()
        object Success: AuthState()
        data class ValidationError(val message: String): AuthState()
        data class Error(val message: String): AuthState()
    }
}