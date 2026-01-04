package com.example.programmingc.presentation.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.programmingc.domain.usecase.ResetPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecoveryViewModel @Inject constructor(
    private val resetPasswordUseCase: ResetPasswordUseCase
): ViewModel() {
    private val _recoveryState = MutableStateFlow<RecoveryState>(RecoveryState.Idle)
    val recoveryState: StateFlow<RecoveryState> = _recoveryState.asStateFlow()

    fun resetPassword(normalizedEmail: String) {
        if (normalizedEmail.isNotEmpty()){
                viewModelScope.launch {
                    _recoveryState.value = RecoveryState.Idle

                    val rc = resetPasswordUseCase.invoke(normalizedEmail)

                    if (rc){
                        _recoveryState.value = RecoveryState.Success("Password reset email sent successfully")
                    }
                    else{
                        _recoveryState.value = RecoveryState.Error("Failed to send reset email. Please try again.")
                    }
            }
        }
        else{
            _recoveryState.value = RecoveryState.Error("Please enter a valid email address")
        }
    }

    sealed class RecoveryState{
        object Idle: RecoveryState()
        data class Success(val message: String): RecoveryState()
        data class Error(val message: String): RecoveryState()
    }
}