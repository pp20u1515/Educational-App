package com.example.programmingc.data.repository

import com.example.programmingc.data.datasource.local.service.CredentialDaoService
import com.example.programmingc.data.datasource.local.service.UserDaoService
import com.example.programmingc.datasource.remote.service.NetworkDaoService
import com.example.programmingc.domain.model.Credential
import com.example.programmingc.domain.model.User
import com.example.programmingc.domain.repo.ICredentialRepository
import com.google.firebase.auth.FirebaseUser
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class CredentialRepositoryTest {
    private lateinit var mockCredentialDaoService: CredentialDaoService
    private lateinit var mockUserDaoService: UserDaoService
    private lateinit var mockNetworkDaoService: NetworkDaoService
    private lateinit var credentialRepository: CredentialRepository

    @Before
    fun setUp(){
        mockCredentialDaoService = mockk()
        mockUserDaoService = mockk()
        mockNetworkDaoService = mockk()
        credentialRepository = CredentialRepository(mockCredentialDaoService, mockUserDaoService, mockNetworkDaoService)
    }

    @Test
    fun `authenticate should save user and credential when network authentication succeeds`() = runTest {
        val credentail = Credential(
            id = 1,
            email = "test@gmail.com",
            password = "test123"
        )
        val user = User(
            id = "1",
            email = "test@gmail.com",
            password = "test123"
        )

        coEvery { mockNetworkDaoService.authenticate(credentail) } returns user
        coEvery { mockUserDaoService.insert(user) } just runs
        coEvery { mockCredentialDaoService.insert(credentail) } just runs

        val result = credentialRepository.authenticate(credentail)

        coVerify(exactly = 1) { mockNetworkDaoService.authenticate(credentail) }
        coVerify(exactly = 1) { mockUserDaoService.insert(user) }
        coVerify(exactly = 1) { mockCredentialDaoService.insert(credentail) }

        assertEquals(true, result)
    }

    @Test
    fun `authenticate shouldnt save user and credential when network authentication fails`() = runTest {
        val credential = Credential(
            id = 1,
            email = "test@gmail.com",
            password = "test123"
        )
        val user = User(
            id = "1",
            email = "test@gmail.com",
            password = "test123"
        )

        coEvery { mockNetworkDaoService.authenticate(credential) } returns null
        coEvery { mockUserDaoService.insert(user) } just runs
        coEvery { mockCredentialDaoService.insert(credential) } just runs

        val result = credentialRepository.authenticate(credential)

        coVerify(exactly = 1) { mockNetworkDaoService.authenticate(credential) }
        coVerify(exactly = 0) { mockUserDaoService.insert(user) }
        coVerify(exactly = 0) { mockCredentialDaoService.insert(credential) }
        assertEquals(false, result)
    }

    @Test
    fun `createAcc should save credential and return user when network registration succeeds`() = runTest {
        val credential = Credential(
            id = 1,
            email = "test@gmail.com",
            password = "test123"
        )
        val mockFirebaseUser = mockk<FirebaseUser>()

        coEvery { mockNetworkDaoService.register(credential) } returns mockFirebaseUser
        coEvery { mockCredentialDaoService.insert(credential) } just runs

        val result = credentialRepository.createAcc(credential)

        coVerify(exactly = 1) { mockNetworkDaoService.register(credential) }
        coVerify(exactly = 1) { mockCredentialDaoService.insert(credential) }
        assertEquals(mockFirebaseUser, result)
    }

    @Test
    fun `createAcc should not save credential and return user when network registration fails`() = runTest {
        val credential = Credential(
            id = 1,
            email = "test@gmail.com",
            password = "test123"
        )

        coEvery { mockNetworkDaoService.register(credential) } returns null
        coEvery { mockCredentialDaoService.insert(credential) } just runs

        val result = credentialRepository.createAcc(credential)

        coVerify(exactly = 1) { mockNetworkDaoService.register(credential) }
        coVerify(exactly = 0) { mockCredentialDaoService.insert(credential) }
        assertEquals(null, result)
    }

    @Test
    fun `resetPassword was successfully reseted`() = runTest {
        val testEmail = "test@gmail.com"

        coEvery { mockNetworkDaoService.resetPassword(testEmail) } returns true

        val result = credentialRepository.resetPassword(testEmail)

        coVerify(exactly = 1) { mockNetworkDaoService.resetPassword(testEmail) }
        assertEquals(true, result)
    }

    @Test
    fun `resetPassword should return false when email not exist`() = runTest {
        val testEmail = "test@gmail.com"

        coEvery { mockNetworkDaoService.resetPassword(testEmail) } returns false

        val result = credentialRepository.resetPassword(testEmail)

        coVerify(exactly = 1) { mockNetworkDaoService.resetPassword(testEmail) }
        assertEquals(false, result)
    }

    @Test
    fun `resetPassword handle when email is empty`() = runTest {
        val testEmail = ""

        coEvery { mockNetworkDaoService.resetPassword(testEmail) } returns false

        val result = credentialRepository.resetPassword(testEmail)

        coVerify(exactly = 1) { mockNetworkDaoService.resetPassword(testEmail) }
        assertEquals(false, result)
    }

    @Test
    fun `repository implement ICredentialRepository interface`() = runTest {
        assertTrue(credentialRepository is ICredentialRepository)
    }
}