package com.example.programmingc.data.datasource.local.service

import com.example.databasedependencies.dao.UserDao
import com.example.databasedependencies.db.Database
import com.example.programmingc.data.datasource.local.mapper.toEntity
import com.example.programmingc.domain.model.User
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class UserDaoServiceTest {
    private lateinit var mockDatabase: Database
    private lateinit var mockUserDaoService: UserDao
    private lateinit var service: UserDaoService

    @Before
    fun setUp(){
        mockDatabase = mockk()
        mockUserDaoService = mockk()

        every { mockDatabase.getUserDao() } returns mockUserDaoService

        service = UserDaoService(mockDatabase)
    }

    @Test
    fun `insert should call dao insert(normal test)`() = runTest {
        val user = User(
            id = "1",
            email = "test@gmail.com",
            password = "test123",
            registrationDate = System.currentTimeMillis()
        )
        val expectedEntity = user.toEntity()

        coEvery { mockUserDaoService.insert(expectedEntity) } just runs
        service.insert(user)
        coVerify(exactly = 1) { mockUserDaoService.insert(expectedEntity) }
    }

    @Test
    fun `readAll should call dao readAll (normal test)`() = runTest {
        val user1 = User("1", "test1@gmail.com", "password1", System.currentTimeMillis())
        val user2 = User("2", "test2@gmail.com", "password2", System.currentTimeMillis())
        val user3 = User("3", "test3@gmail.com", "password3")
        val user4 = User("4", "test4@gmail.com", "password4")
        val entities = listOf(user1.toEntity(), user2.toEntity(), user3.toEntity(), user4.toEntity())

        every { mockUserDaoService.readAll() } returns flowOf(entities)

        val result = service.readAll()
        verify (exactly = 1) { mockUserDaoService.readAll() }

        val currentResults = result.toList()
        assertEquals(1, currentResults.size, "Flows should emit once")

        val actualUsers = currentResults.first()
        assertEquals(4, actualUsers.size)

        assertEquals(user1.id, actualUsers[0].id)
        assertEquals(user1.email, actualUsers[0].email)
        assertEquals(user1.password, actualUsers[0].password)
        assertEquals(user1.registrationDate, actualUsers[0].registrationDate)

        assertEquals(user2.id, actualUsers[1].id)
        assertEquals(user2.email, actualUsers[1].email)
        assertEquals(user2.password, actualUsers[1].password)
        assertEquals(user2.registrationDate, actualUsers[1].registrationDate)

        assertEquals(user3.id, actualUsers[2].id)
        assertEquals(user3.email, actualUsers[2].email)
        assertEquals(user3.password, actualUsers[2].password)
        assertEquals(user3.registrationDate, actualUsers[2].registrationDate)

        assertEquals(user4.id, actualUsers[3].id)
        assertEquals(user4.email, actualUsers[3].email)
        assertEquals(user4.password, actualUsers[3].password)
        assertEquals(user4.registrationDate, actualUsers[3].registrationDate)
    }

    @Test
    fun `readAll should call dao readAll when db is empty`() = runTest {
        every { mockUserDaoService.readAll() } returns flowOf(emptyList())

        val result = service.readAll()

        verify(exactly = 1) { mockUserDaoService.readAll() }

        val currentResults = result.toList()
        assertEquals(1, currentResults.size, "Flow should emit once")

        val actualUsers = currentResults.first()
        assertTrue(actualUsers.isEmpty(), "Should return empty list when DB is empty")
    }

    @Test
    fun `readByEmail should call dao readByEmail when user exist`() = runTest {
        val searchedEmail = "test@gmail.com"
        val user = User(
            id = "1",
            email = searchedEmail,
            password = "test1234"
        )
        val expectedEntity = user.toEntity()

        coEvery { mockUserDaoService.readByEmail(searchedEmail) } returns expectedEntity

        val result = service.readByEmail(searchedEmail)

        coVerify(exactly = 1) { mockUserDaoService.readByEmail(searchedEmail) }
        assertNotNull(result)
        assertEquals(searchedEmail, result?.email)
        assertEquals(user.id, result.id)
        assertEquals(user.password, result.password)
        assertEquals(user.registrationDate, result.registrationDate)
    }

    @Test
    fun `readByEmail should call dao readByEmail whith non-existent email`() = runTest {
        val searchedEmail = "searched_email@gmai.com"

        coEvery { mockUserDaoService.readByEmail(searchedEmail) } returns null

        val result = service.readByEmail(searchedEmail)

        coVerify(exactly = 1) { mockUserDaoService.readByEmail(searchedEmail) }
        assertNull(result)
    }

    @Test
    fun `update should call dao update (normal test)`() = runTest {
        val user = User(
            id = "2",
            email = "test@gmail.com",
            password = "test"
        )
        val expectedEntity = user.toEntity()

        coEvery { mockUserDaoService.update(expectedEntity) } just runs
        service.update(user)
        coVerify(exactly = 1) { mockUserDaoService.update(expectedEntity) }
    }

    @Test
    fun `delete should call dao delete (normal test)`() = runTest {
        val user = User(
            id = "2",
            email = "test@gmail.com",
            password = "test"
        )
        val expectedEntity = user.toEntity()

        coEvery { mockUserDaoService.delete(expectedEntity) } just runs
        service.delete(user)
        coVerify(exactly = 1) { mockUserDaoService.delete(expectedEntity) }
    }
}