package com.example.programmingc.data.repository

import com.example.programmingc.data.datasource.local.service.UserDaoService
import com.example.programmingc.data.datasource.remote.service.INetworkDaoService
import com.example.programmingc.domain.model.Credential
import com.example.programmingc.domain.model.User
import com.example.programmingc.domain.repo.IAuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class AuthRepositoryTest {
    private lateinit var mockFirebaseAuth: FirebaseAuth
    private lateinit var mockNetworkDaoService: INetworkDaoService
    private lateinit var mockUserDaoService: UserDaoService
    private lateinit var authRepository: AuthRepository

    @Before
    fun setUp(){
        mockFirebaseAuth = mockk()
        mockNetworkDaoService = mockk()
        mockUserDaoService = mockk()
        authRepository = AuthRepository(mockFirebaseAuth, mockNetworkDaoService, mockUserDaoService)
    }

    @Test
    fun `isUserAuthenticated should return true when FirebaseAuth has current user`() = runTest{
        val mockFirebaseUser = mockk<FirebaseUser>()
        every { mockFirebaseAuth.currentUser } returns mockFirebaseUser

        val result = authRepository.isUserAuthenticated()

        verify(exactly = 1) { mockFirebaseAuth.currentUser }
        assertTrue(result)
    }

    @Test
    fun `isUserAuthenticated should return false when FirebaseAuth doesnt has current user`() = runTest{
        every { mockFirebaseAuth.currentUser } returns null

        val result = authRepository.isUserAuthenticated()

        verify(exactly = 1) { mockFirebaseAuth.currentUser }
        assertFalse ("Should return false when user is not authenticated", result)
    }

    @Test
    fun `signOut should return true when FirebaseAuth signOut succeeds`() = runTest {
        every { mockFirebaseAuth.signOut() } just runs

        val result = authRepository.signOut()

        verify(exactly = 1) { mockFirebaseAuth.signOut() }
        assertTrue(result)
    }

    @Test
    fun `signOut should return false when FirebaseAuth signOut fails`() = runTest {
        val exception = RuntimeException("Firebase auth error")
        every { mockFirebaseAuth.signOut() } throws exception

        val result = authRepository.signOut()

        verify(exactly = 1) { mockFirebaseAuth.signOut() }
        assertFalse(result)
    }

    @Test
    fun `signOut should handle different types of exceptions`() = runTest {
        val exceptions = listOf(
            RuntimeException("Network error"),
            IllegalStateException("Auth not initialized"),
            Exception("Generic error")
        )
        exceptions.forEach { exception ->
            every { mockFirebaseAuth.signOut() } throws exception

            val result = authRepository.signOut()

            assertFalse(result)
            clearMocks(mockFirebaseAuth)
        }
    }

    @Test
    fun `isUserAuthenticated should handle multiple calls correctly`() = runTest {
        val mockUser = mockk<FirebaseUser>()

        every { mockFirebaseAuth.currentUser } returnsMany listOf(null, mockUser, null)

        assertFalse("First call should return false", authRepository.isUserAuthenticated())
        assertTrue(authRepository.isUserAuthenticated())
        assertFalse("Third call should return false", authRepository.isUserAuthenticated())

        verify(exactly = 3) { mockFirebaseAuth.currentUser }
    }

    @Test
    fun `signOut should be called exactly once per invocation`() = runTest {
        var callCount = 0

        every { mockFirebaseAuth.signOut() } answers { callCount++ }

        authRepository.signOut()
        authRepository.signOut()

        verify(exactly = 2) { mockFirebaseAuth.signOut() }
        assertEquals( 2, callCount)
    }

    @Test
    fun `AuthRepository should implement IAuthRepository interface`() {
        assertTrue(authRepository is IAuthRepository)
    }

    @Test
    fun `AuthRepository should depend on FirebaseAuth`() = runTest {
        val customMockAuth = mockk<FirebaseAuth>()
        every { customMockAuth.currentUser } returns mockk()

        val customRepository = AuthRepository(customMockAuth, mockNetworkDaoService, mockUserDaoService)
        val result = customRepository.isUserAuthenticated()

        verify(exactly = 1) { customMockAuth.currentUser }
        assertTrue(result)
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

        val result = authRepository.authenticate(credentail)

        coVerify(exactly = 1) { mockNetworkDaoService.authenticate(credentail) }
        coVerify(exactly = 1) { mockUserDaoService.insert(user) }

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

        val result = authRepository.authenticate(credential)

        coVerify(exactly = 1) { mockNetworkDaoService.authenticate(credential) }
        coVerify(exactly = 0) { mockUserDaoService.insert(user) }
        assertEquals(false, result)
    }

    @Test
    fun `createAcc should save credential and return user when network registration succeeds`() = runTest {
        val credential = Credential(
            id = 1,
            email = "test@gmail.com",
            password = "test123"
        )
        val mockFirebaseUser = mockk<FirebaseUser> {
            every { uid } returns "firebase_uid_123"
            every { email } returns "test@gmail.com"
            every { displayName } returns "Test Display Name"
        }

        coEvery { mockNetworkDaoService.register(credential) } returns mockFirebaseUser
        coEvery { mockUserDaoService.insert(any()) } just runs

        val result = authRepository.createAcc(credential)

        coVerify(exactly = 1) { mockNetworkDaoService.register(credential) }
        coVerify(exactly = 1) { mockUserDaoService.insert(any()) }
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
        coEvery { mockUserDaoService.insert(any()) } just runs

        val result = authRepository.createAcc(credential)

        coVerify(exactly = 1) { mockNetworkDaoService.register(credential) }
        coVerify(exactly = 0) { mockUserDaoService.insert(any()) }
        assertEquals(null, result)
    }

    @Test
    fun `resetPassword was successfully reseted`() = runTest {
        val testEmail = "test@gmail.com"

        coEvery { mockNetworkDaoService.resetPassword(testEmail) } returns true

        val result = authRepository.resetPassword(testEmail)

        coVerify(exactly = 1) { mockNetworkDaoService.resetPassword(testEmail) }
        assertEquals(true, result)
    }

    @Test
    fun `resetPassword should return false when email not exist`() = runTest {
        val testEmail = "test@gmail.com"

        coEvery { mockNetworkDaoService.resetPassword(testEmail) } returns false

        val result = authRepository.resetPassword(testEmail)

        coVerify(exactly = 1) { mockNetworkDaoService.resetPassword(testEmail) }
        assertEquals(false, result)
    }

    @Test
    fun `resetPassword handle when email is empty`() = runTest {
        val testEmail = ""

        coEvery { mockNetworkDaoService.resetPassword(testEmail) } returns false

        val result = authRepository.resetPassword(testEmail)

        coVerify(exactly = 1) { mockNetworkDaoService.resetPassword(testEmail) }
        assertEquals(false, result)
    }
}