package com.example.programmingc.presentation.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.programmingc.presentation.ui.manager.AuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authManager: AuthManager
) : ViewModel() {
    val menuBarVisible = MutableLiveData(true)

    val authState = authManager.authState



    fun checkAuthState() = viewModelScope.launch {
        authManager.checkAuthState()
    }

    fun authenticate(email: String, password: String) = viewModelScope.launch {
        authManager.authenticate(email, password)
    }

    fun signOut() {
        authManager.signOut()
    }
}