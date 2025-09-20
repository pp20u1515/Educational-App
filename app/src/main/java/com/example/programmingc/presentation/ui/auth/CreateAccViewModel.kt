package com.example.programmingc.presentation.ui.auth

import androidx.lifecycle.ViewModel
import com.example.programmingc.domain.model.Credential
import com.example.programmingc.domain.usecase.CreateAccUseCase
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateAccViewModel @Inject constructor(
    private val createAccUseCase: CreateAccUseCase
): ViewModel() {
    suspend fun createAcc(credential: Credential): FirebaseUser?{
        return createAccUseCase.invoke(credential)
    }
}