package com.example.programmingc.domain.usecase

import com.example.programmingc.domain.model.Credential
import com.example.programmingc.domain.repo.IAuthRepository
import com.google.firebase.auth.FirebaseUser
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class CreateAccUseCaseTest {
    private lateinit var mockAuthRepository: IAuthRepository
    private lateinit var createAccUseCase: CreateAccUseCase

    @Before
    fun setUp(){
        mockAuthRepository = mockk()
        createAccUseCase = CreateAccUseCase(mockAuthRepository)
    }

    // normal test with valid credential
    @Test
    fun normalTest() = runTest {
        val credential = Credential(
            email = "test@gmail.com",
            password = "test12345"
        )
        val mockFirebaseUser = mockk<FirebaseUser>()
        coEvery { mockAuthRepository.createAcc(credential) } returns mockFirebaseUser

        val result = createAccUseCase.invoke(credential)

        coVerify(exactly = 1) { mockAuthRepository.createAcc(credential) }

        assertNotNull(result)
        assertEquals(mockFirebaseUser, result)
    }

    // test with empty email and password
    @Test
    fun negTestWithEmptyEmailPassword() = runTest {
        val credential = Credential(
            email = "",
            password = ""
        )

        coEvery { mockAuthRepository.createAcc(credential) } returns null

        val result = createAccUseCase.invoke(credential)

        coVerify(exactly = 1) { mockAuthRepository.createAcc(credential) }
        assertNull(result)
    }

    // test with invalid credential
    @Test
    fun negTestWithInvalidCredential() = runTest {
        val credential = Credential(
            email = "non-existent@something",
            password = "wrongPassword"
        )

        coEvery { mockAuthRepository.createAcc(credential) } returns null

        val result = createAccUseCase.invoke(credential)

        coVerify(exactly = 1) { mockAuthRepository.createAcc(credential) }
        assertNull(result)
    }

    // test with weak password
    @Test
    fun negTestWithWeakPassword() = runTest {
        val credential = Credential(
            email = "non-existent@something",
            password = "wro"
        )

        coEvery { mockAuthRepository.createAcc(credential) } returns null

        val result = createAccUseCase.invoke(credential)

        coVerify(exactly = 1) { mockAuthRepository.createAcc(credential) }
        assertNull(result)
    }

    // test with same credentials multiple times
    @Test
    fun normalTestMultipleTimes() = runTest {
        val credential = Credential(
            email = "test@gmail.com",
            password = "test12345"
        )
        val mockFirebaseUser = mockk<FirebaseUser>()

        coEvery { mockAuthRepository.createAcc(credential) } returns mockFirebaseUser

        val result1 = createAccUseCase.invoke(credential)
        val result2 = createAccUseCase.invoke(credential)

        coVerify(exactly = 2) { mockAuthRepository.createAcc(credential) }

        assertNotNull(result1)
        assertNotNull(result2)
        assertEquals(mockFirebaseUser, result1)
        assertEquals(mockFirebaseUser, result2)
    }
}