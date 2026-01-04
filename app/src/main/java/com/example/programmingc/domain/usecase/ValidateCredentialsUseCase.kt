package com.example.programmingc.domain.usecase

import com.example.programmingc.domain.model.ValidAuthInput
import com.example.programmingc.domain.validate_input.checkInput
import javax.inject.Inject

class ValidateCredentialsUseCase @Inject constructor() {
    operator fun invoke(email: String, password: String): ValidationResult{
        val rc = checkInput(email, password)
        
        return when (rc){
            ValidAuthInput.OK -> ValidationResult.Success(email, password)
            ValidAuthInput.EMPTY_EMAIL -> ValidationResult.Error("You forgot to input the email")
            ValidAuthInput.EMPTY_PASSWORD -> ValidationResult.Error("You forgot to write your password")
            ValidAuthInput.SHORT_PASSWORD -> ValidationResult.Error("Password should be longer then 6 symbols")
            ValidAuthInput.NOT_VALID_EMAIL -> ValidationResult.Error("You entered not correct email")
        }
    }

    sealed class ValidationResult{
        data class Success(
            val email: String,
            val password: String): ValidationResult()
        data class Error(val message: String): ValidationResult()
    }
}