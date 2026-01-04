package com.example.programmingc.domain.usecase

import com.example.programmingc.domain.repo.IAuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ResetPasswordUseCaseTest {
    private lateinit var mockAuthRepository: IAuthRepository
    private lateinit var resetPasswordUseCase: ResetPasswordUseCase

    @Before
    fun setUp(){
        mockAuthRepository = mockk()
        resetPasswordUseCase = ResetPasswordUseCase(mockAuthRepository)
    }

    // normal test with valid email
    @Test
    fun normalTest() = runTest {
        val email = "test@gmail.com"

        coEvery { mockAuthRepository.resetPassword(email) } returns true

        val result = resetPasswordUseCase.invoke(email)

        coVerify(exactly = 1) { mockAuthRepository.resetPassword(email) }
        assertTrue(result)
    }

    // test with empty email
    @Test
    fun negTestWithEmptyEmail() = runTest {
        val email = ""

        coEvery { mockAuthRepository.resetPassword(email) } returns false
        val result = resetPasswordUseCase.invoke(email)

        coVerify(exactly = 1) { mockAuthRepository.resetPassword(email) }
        assertFalse(result)
    }

    // test with non-existent email
    @Test
    fun negTestWithNonExistentEmail() = runTest {
        val email = "nonexistent@gmail.com"

        coEvery { mockAuthRepository.resetPassword(email) } returns false
        val result = resetPasswordUseCase.invoke(email)

        coVerify(exactly = 1) { mockAuthRepository.resetPassword(email) }
        assertFalse(result)
    }

    // test with invalid email
    @Test
    fun negTestWithInvalidEmail() = runTest {
        val email = "invalid@gggmail.comos"

        coEvery { mockAuthRepository.resetPassword(email) } returns false
        val result = resetPasswordUseCase.invoke(email)

        coVerify(exactly = 1) { mockAuthRepository.resetPassword(email) }
        assertFalse(result)
    }

    // test with same email multiple times
    @Test
    fun normalTestMultipleTimes() = runTest {
        val email = "test@gmail.com"

        coEvery { mockAuthRepository.resetPassword(email) } returns true

        val result1 = resetPasswordUseCase.invoke(email)
        val result2 = resetPasswordUseCase.invoke(email)

        coVerify(exactly = 2) { mockAuthRepository.resetPassword(email) }
        assertTrue(result1)
        assertTrue(result2)
    }
}