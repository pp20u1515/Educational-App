package com.example.programmingc.presentation.auth

import com.example.programmingc.domain.model.Credential
import com.example.programmingc.domain.usecase.AuthenticateUseCase
import com.example.programmingc.domain.usecase.ValidateCredentialsUseCase
import com.example.programmingc.presentation.ui.auth.AuthViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class AuthVMTest {
    private lateinit var mockAuthenticateUseCase: AuthenticateUseCase
    private lateinit var mockValidateCredentialUseCase: ValidateCredentialsUseCase
    private lateinit var viewModel: AuthViewModel

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setUp(){
        Dispatchers.setMain(testDispatcher)
        mockAuthenticateUseCase = mockk()
        mockValidateCredentialUseCase = mockk()
        viewModel = AuthViewModel(mockAuthenticateUseCase, mockValidateCredentialUseCase)
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    // test when entered credential is correct
    @Test
    fun normalTestWithCorrectCredential() = testScope.runTest {
        val email = "test@gmail.com"
        val password = "test1239"

        coEvery { mockValidateCredentialUseCase.invoke(email, password)
        } returns ValidateCredentialsUseCase.ValidationResult.Success(email, password)
        coEvery { mockAuthenticateUseCase.invoke(Credential(email = email, password = password)) } returns true

        val states = mutableListOf<AuthViewModel.AuthState>()
        val job = testScope.launch {
            viewModel.authState.collect { states.add(it) }
        }

        viewModel.onLogInClick(email, password)
        advanceUntilIdle()

        assertEquals(2, states.size)
        assertEquals(AuthViewModel.AuthState.Idle, states[0])
        assertEquals(AuthViewModel.AuthState.Success, states[1])

        job.cancel()
    }

    // test when email is not correct
    @Test
    fun negTestWithInvalidEmail() = testScope.runTest {
        val email = "invalid-email.com"
        val password = "test12345"

        coEvery { mockValidateCredentialUseCase.invoke(email, password)
        } returns ValidateCredentialsUseCase.ValidationResult.Error("Invalid email format")

        val states = mutableListOf<AuthViewModel.AuthState>()
        val job = testScope.launch {
            viewModel.authState.collect { states.add(it) }
        }

        viewModel.onLogInClick(email, password)
        advanceUntilIdle()

        assertEquals(2, states.size)
        assertEquals(AuthViewModel.AuthState.Idle, states[0])
        assertTrue(states[1] is AuthViewModel.AuthState.ValidationError)

        val errorState = states[1] as AuthViewModel.AuthState.ValidationError
        assertEquals("Invalid email format", errorState.message)

        job.cancel()
    }

    // test when email is empty
    @Test
    fun negTestWhenEmailEmpty() = testScope.runTest {
        val email = ""
        val password = "test12345"

        coEvery { mockValidateCredentialUseCase.invoke(email, password)
        } returns ValidateCredentialsUseCase.ValidationResult.Error("Invalid email format")

        val states = mutableListOf<AuthViewModel.AuthState>()
        val job = testScope.launch {
            viewModel.authState.collect { states.add(it) }
        }

        viewModel.onLogInClick(email, password)
        advanceUntilIdle()

        assertEquals(2, states.size)
        assertEquals(AuthViewModel.AuthState.Idle, states[0])
        assertTrue(states[1] is AuthViewModel.AuthState.ValidationError)

        val errorState = states[1] as AuthViewModel.AuthState.ValidationError
        assertEquals("Invalid email format", errorState.message)

        job.cancel()
    }

    // test when entered wrong password
    @Test
    fun negTestWithInvalidPassword() = testScope.runTest {
        val email = "test@gmail.com"
        val password = "wrongpassword"

        coEvery { mockValidateCredentialUseCase.invoke(email, password)
        } returns ValidateCredentialsUseCase.ValidationResult.Error("Entered wrong password")

        val states = mutableListOf<AuthViewModel.AuthState>()
        val job = testScope.launch {
            viewModel.authState.collect { states.add(it) }
        }

        viewModel.onLogInClick(email, password)
        advanceUntilIdle()

        assertEquals(2, states.size)
        assertEquals(AuthViewModel.AuthState.Idle, states[0])
        assertTrue(states[1] is AuthViewModel.AuthState.ValidationError)

        val errorState = states[1] as AuthViewModel.AuthState.ValidationError
        assertEquals("Entered wrong password", errorState.message)

        job.cancel()
    }

    // test when length of the password is short
    @Test
    fun negTestWithShortPassword() = testScope.runTest {
        val email = "test@gmail.com"
        val password = "asd"

        coEvery { mockValidateCredentialUseCase.invoke(email, password)
        } returns ValidateCredentialsUseCase.ValidationResult.Error("Invalid password format")

        val states = mutableListOf<AuthViewModel.AuthState>()
        val job = testScope.launch {
            viewModel.authState.collect { states.add(it) }
        }

        viewModel.onLogInClick(email, password)
        advanceUntilIdle()

        assertEquals(2, states.size)
        assertEquals(AuthViewModel.AuthState.Idle, states[0])
        assertTrue(states[1] is AuthViewModel.AuthState.ValidationError)

        val errorState = states[1] as AuthViewModel.AuthState.ValidationError
        assertEquals("Invalid password format", errorState.message)

        job.cancel()
    }

    // test when pasword is empty
    @Test
    fun negTestWhenPasswordEmpty() = testScope.runTest {
        val email = "test@gmail.com"
        val password = ""

        coEvery { mockValidateCredentialUseCase.invoke(email, password)
        } returns ValidateCredentialsUseCase.ValidationResult.Error("Invalid password format")

        val states = mutableListOf<AuthViewModel.AuthState>()
        val job = testScope.launch {
            viewModel.authState.collect { states.add(it) }
        }

        viewModel.onLogInClick(email, password)
        advanceUntilIdle()

        assertEquals(2, states.size)
        assertEquals(AuthViewModel.AuthState.Idle, states[0])
        assertTrue(states[1] is AuthViewModel.AuthState.ValidationError)

        val errorState = states[1] as AuthViewModel.AuthState.ValidationError
        assertEquals("Invalid password format", errorState.message)

        job.cancel()
    }

    // test with network error
    @Test
    fun negTestWithNetworkError() = testScope.runTest {
        val email = "test@gmail.com"
        val password = "test12344"

        coEvery { mockValidateCredentialUseCase.invoke(email, password)
        } returns ValidateCredentialsUseCase.ValidationResult.Success(email, password)

        coEvery { mockAuthenticateUseCase.invoke(Credential(email = email, password = password)) } returns false

        val states = mutableListOf<AuthViewModel.AuthState>()
        val job = testScope.launch {
            viewModel.authState.collect { states.add(it) }
        }

        viewModel.onLogInClick(email, password)
        advanceUntilIdle()

        assertEquals(2, states.size)
        assertEquals(AuthViewModel.AuthState.Idle, states[0])
        assertTrue(states[1] is AuthViewModel.AuthState.Error)

        val errorState = states[1] as AuthViewModel.AuthState.Error
        assertEquals("Error!", errorState.message)

        job.cancel()
    }
}