package com.example.programmingc.domain.usecase

import com.example.programmingc.domain.model.ValidAuthInput
import com.example.programmingc.domain.validate_input.checkInput
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ValidateCredentialsUseCaseTest {
    private lateinit var validateCredentialsUseCase: ValidateCredentialsUseCase

    @Before
    fun setUp(){
        validateCredentialsUseCase = ValidateCredentialsUseCase()
    }

    @After
    fun tearDown(){
        unmockkStatic(::checkInput)
    }

    // normal test with valid email and password
    @Test
    fun normalTest(){
        val email = "test@gmail.com"
        val password = "test12345"

        mockkStatic(::checkInput)
        every { checkInput(email, password) } returns ValidAuthInput.OK

        val result = validateCredentialsUseCase.invoke(email, password)

        assertTrue(result is ValidateCredentialsUseCase.ValidationResult.Success)

        val successResult = result as ValidateCredentialsUseCase.ValidationResult.Success
        assertEquals(email, successResult.email)
        assertEquals(password, successResult.password)

        unmockkStatic(::checkInput)
    }

    // test with empty email
    @Test
    fun negTestWithEmptyEmail(){
        val email = ""
        val password = "test12345"

        mockkStatic(::checkInput)
        every { checkInput(email, password) } returns ValidAuthInput.EMPTY_EMAIL

        val result = validateCredentialsUseCase.invoke(email, password)

        assertTrue(result is ValidateCredentialsUseCase.ValidationResult.Error)

        val errorResult = result as ValidateCredentialsUseCase.ValidationResult.Error
        assertEquals("You forgot to input the email", errorResult.message)

        unmockkStatic(::checkInput)
    }

    // test with invalid email
    @Test
    fun negTestWithInvalidEmail(){
        val email = "test.test.com"
        val password = "test12345"

        mockkStatic(::checkInput)
        every { checkInput(email, password) } returns ValidAuthInput.NOT_VALID_EMAIL

        val result = validateCredentialsUseCase.invoke(email, password)

        assertTrue(result is ValidateCredentialsUseCase.ValidationResult.Error)

        val errorResult = result as ValidateCredentialsUseCase.ValidationResult.Error
        assertEquals("You entered not correct email", errorResult.message)

        unmockkStatic(::checkInput)
    }

    // test with empty password
    @Test
    fun negTestWithEmptyPassword(){
        val email = "test@gmail.com"
        val password = ""

        mockkStatic(::checkInput)
        every { checkInput(email, password) } returns ValidAuthInput.EMPTY_PASSWORD

        val result = validateCredentialsUseCase.invoke(email, password)

        assertTrue(result is ValidateCredentialsUseCase.ValidationResult.Error)

        val errorResult = result as ValidateCredentialsUseCase.ValidationResult.Error
        assertEquals("You forgot to write your password", errorResult.message)

        unmockkStatic(::checkInput)
    }

    // test with short password
    @Test
    fun negTestWithShortPassword(){
        val email = "test@gmail.com"
        val password = "word"

        mockkStatic(::checkInput)
        every { checkInput(email, password) } returns ValidAuthInput.SHORT_PASSWORD

        val result = validateCredentialsUseCase.invoke(email, password)

        assertTrue(result is ValidateCredentialsUseCase.ValidationResult.Error)

        val errorResult = result as ValidateCredentialsUseCase.ValidationResult.Error
        assertEquals("Password should be longer then 6 symbols", errorResult.message)

        unmockkStatic(::checkInput)
    }

    // test when email with special characters provided
    @Test
    fun normalTestWithSpecialCharsInEmail() {
        // Given
        val email = "user.name+tag@yahoo.com"
        val password = "password123"

        mockkStatic(::checkInput)
        every { checkInput(email, password) } returns ValidAuthInput.OK

        val result = validateCredentialsUseCase.invoke(email, password)

        assertTrue(result is ValidateCredentialsUseCase.ValidationResult.Success)

        unmockkStatic(::checkInput)
    }
}