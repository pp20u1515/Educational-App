package com.example.programmingc.presentation.auth

import com.example.programmingc.domain.usecase.ResetPasswordUseCase
import com.example.programmingc.presentation.ui.auth.RecoveryViewModel
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
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class RecoveryVMTest {
    private lateinit var mockResetPasswordUseCase: ResetPasswordUseCase
    private lateinit var viewModel: RecoveryViewModel

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setUp(){
        Dispatchers.setMain(testDispatcher)

        mockResetPasswordUseCase = mockk()
        viewModel = RecoveryViewModel(mockResetPasswordUseCase)
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }

    // normal test with valid email
    @Test
    fun normalTest() = testScope.runTest {
        val email = "test@gmail.com"

        coEvery { mockResetPasswordUseCase.invoke(email) } returns true

        val states = mutableListOf<RecoveryViewModel.RecoveryState>()
        val job = testScope.launch {
            viewModel.recoveryState.collect { states.add(it) }
        }

        viewModel.resetPassword(email)
        advanceUntilIdle()

        assertEquals(2, states.size)
        assertEquals(RecoveryViewModel.RecoveryState.Idle, states[0])
        assertTrue(states[1] is RecoveryViewModel.RecoveryState.Success)

        val successState = states[1] as RecoveryViewModel.RecoveryState.Success
        assertEquals("Password reset email sent successfully", successState.message)

        job.cancel()
    }

    // test when email doesnt exist in db
    @Test
    fun negTestWithNonExistentEmail() = testScope.runTest {
        val email = "non-existent@gmail"

        coEvery { mockResetPasswordUseCase.invoke(email) } returns false

        val states = mutableListOf<RecoveryViewModel.RecoveryState>()
        val job = testScope.launch {
            viewModel.recoveryState.collect { states.add(it) }
        }

        viewModel.resetPassword(email)
        advanceUntilIdle()

        assertEquals(2, states.size)
        assertEquals(RecoveryViewModel.RecoveryState.Idle, states[0])
        assertTrue(states[1] is RecoveryViewModel.RecoveryState.Error)

        val errorState = states[1] as RecoveryViewModel.RecoveryState.Error
        assertEquals("Failed to send reset email. Please try again.", errorState.message)

        job.cancel()
    }

    // test when email is empty
    @Test
    fun negTestWithEmptyEmail() = testScope.runTest {
        val email = ""

        val states = mutableListOf<RecoveryViewModel.RecoveryState>()
        val job = testScope.launch {
            viewModel.recoveryState.collect { states.add(it) }
        }

        viewModel.resetPassword(email)
        advanceUntilIdle()

        assertEquals(1, states.size)
        assertTrue(states[0] is RecoveryViewModel.RecoveryState.Error)

        val errorState = states[0] as RecoveryViewModel.RecoveryState.Error
        assertEquals("Please enter a valid email address", errorState.message)

        job.cancel()
    }
}