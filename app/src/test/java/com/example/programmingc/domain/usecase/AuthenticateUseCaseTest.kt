package com.example.programmingc.domain.usecase

import com.example.programmingc.domain.model.Credential
import com.example.programmingc.domain.repo.IAuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class AuthenticateUseCaseTest {
    private lateinit var mockAuthRepository: IAuthRepository
    private lateinit var authenticateUseCase: AuthenticateUseCase

    @Before
    fun setUp(){
        mockAuthRepository = mockk()
        authenticateUseCase = AuthenticateUseCase(mockAuthRepository)
    }

    // normal test with valid credential
    @Test
    fun normalTestWithValidCredential() = runTest {
        val credential = Credential(
            email = "test@gmail.com",
            password = "test12345"
        )

        coEvery { mockAuthRepository.authenticate(credential) } returns true

        val result = authenticateUseCase.invoke(credential)

        assertTrue(result)
        coVerify(exactly = 1) { mockAuthRepository.authenticate(credential) }
    }

    // test with invalid credentials
    @Test
    fun negTestWithInvalidCredentials() = runTest {
        val credential = Credential(
            email = "non_existent@gmail.com",
            password = "wrongPassword"
        )
        coEvery { mockAuthRepository.authenticate(credential) } returns false

        val result = authenticateUseCase.invoke(credential)

        assertFalse(result)
        coVerify(exactly = 1) { mockAuthRepository.authenticate(credential) }
    }

    // test with same credentials multiple times
    @Test
    fun normalTestMultipletimes() = runTest {
        val credential = Credential(
            email = "test@gmail.com",
            password = "test12345"
        )

        coEvery { mockAuthRepository.authenticate(credential) } returns true

        val result1 = authenticateUseCase.invoke(credential)
        val result2 = authenticateUseCase.invoke(credential)

        assertTrue(result1)
        assertTrue(result2)
        coVerify(exactly = 2) { mockAuthRepository.authenticate(credential) }
    }

    // test with empty email and password
    @Test
    fun negTestWithEmptyEmailPassword() = runTest {
        val credential = Credential(
            email = "",
            password = ""
        )
        coEvery { mockAuthRepository.authenticate(credential) } returns false

        val result = authenticateUseCase.invoke(credential)

        coVerify(exactly = 1) { mockAuthRepository.authenticate(credential) }
        assertFalse(result)
    }
}