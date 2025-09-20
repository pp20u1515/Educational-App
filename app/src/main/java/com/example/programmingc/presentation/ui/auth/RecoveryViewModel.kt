package com.example.programmingc.presentation.ui.auth

import androidx.lifecycle.ViewModel
import com.example.programmingc.domain.model.Credential
import com.example.programmingc.domain.usecase.ResetPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecoveryViewModel @Inject constructor(
    private val resetPasswordUseCase: ResetPasswordUseCase
): ViewModel() {
    suspend fun resetPassword(email: String): Boolean{
        return resetPasswordUseCase.invoke(email)
    }
}