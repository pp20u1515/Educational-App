package com.example.programmingc.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserCredentialsViewData(
    val email: String,
    val password: String,
): Parcelable
