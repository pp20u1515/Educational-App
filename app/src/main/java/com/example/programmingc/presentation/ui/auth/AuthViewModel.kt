package com.example.programmingc.presentation.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authService: FirebaseAuthService
): ViewModel() {
    fun authenticate(email: String, password: String, callback: (Boolean) -> Unit){
        viewModelScope.launch {
            try {
                val result = authService.logIn(email, password)
                callback(result)
            } catch (e: Exception){
                callback(false)
            }
        }
    }
}