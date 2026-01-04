package com.example.programmingc.data.datasource.local.service

import com.example.databasedependencies.db.Database
import com.example.programmingc.data.datasource.local.mapper.toEntity
import com.example.programmingc.domain.model.Credential
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
class CredentialDaoServiceTest {
    private lateinit var mockDatabase: Database
    private lateinit var mockCredentialDao: com.example.databasedependencies.dao.CredentialDao
    private lateinit var service: CredentialDaoService

    @Before
    fun setUp(){
        mockDatabase = mockk()
        mockCredentialDao = mockk()

        every { mockDatabase.getCredentialDao() } returns mockCredentialDao
        service = CredentialDaoService(mockDatabase)
    }

    @Test
    fun `insert should call dao insert with converted entity`() = runTest{
        val credential = Credential(
            id = 1L,
            email = "test@gmail.com",
            password = "test123"
        )
        val expectedEntity = credential.toEntity()

        coEvery { mockCredentialDao.insert(expectedEntity) } just runs
        service.insert(credential)
        coVerify(exactly = 1) { mockCredentialDao.insert(expectedEntity) }
    }

    @Test
    fun `readByEmail should call dao readByEmail with correct email`() = runTest {
        val emailToSearch = "test@gmail.com"
        val credential = Credential(
            id = 1L,
            email = emailToSearch,
            password = "test123"
        )
        val expectedEntity = credential.toEntity()

        coEvery { mockCredentialDao.readByEmail(emailToSearch) } returns expectedEntity

        val result = service.readByEmail(emailToSearch)

        coVerify(exactly = 1) { mockCredentialDao.readByEmail(emailToSearch) }

        assertNotNull(result)
        assertEquals(credential.id, result.id)
        assertEquals(credential.email, result.email)
        assertEquals(credential.password, result.password)
    }

    @Test
    fun `readByEmail should call dao readByEmail with non-existent email`() = runTest {
        val nonExistedEmail = "non_existed@gmail.com"

        coEvery { mockCredentialDao.readByEmail(nonExistedEmail) } returns null

        val result = service.readByEmail(nonExistedEmail)

        coVerify(exactly = 1) { mockCredentialDao.readByEmail(nonExistedEmail) }
        assertNull(result)
    }

    @Test
    fun `readAll should call dao readAll (normal test)`() = runTest{
        val credential1 = Credential(1L, "test1@gmail.com", "test123")
        val credential2 = Credential(2L, "test2@gmail.com", "test123@")
        val credential3 = Credential(3L, "test3@gmail.com", "test123!")
        val entities = listOf(credential1.toEntity(), credential2.toEntity(), credential3.toEntity())

        every { mockCredentialDao.readAll() } returns flowOf(entities)

        val result = service.readAll()

        verify (exactly = 1) { mockCredentialDao.readAll() }

        val collectResults = result.toList()
        assertEquals(1, collectResults.size, "Flow should emit once")

        val actualCredentials = collectResults.first()
        assertEquals(3, actualCredentials.size)

        assertEquals(credential1.id, actualCredentials[0].id)
        assertEquals(credential1.email, actualCredentials[0].email)
        assertEquals(credential1.password, actualCredentials[0].password)

        assertEquals(credential2.id, actualCredentials[1].id)
        assertEquals(credential2.email, actualCredentials[1].email)
        assertEquals(credential2.password, actualCredentials[1].password)

        assertEquals(credential3.id, actualCredentials[2].id)
        assertEquals(credential3.email, actualCredentials[2].email)
        assertEquals(credential3.password, actualCredentials[2].password)
    }

    @Test
    fun `readAll should call dao readAll when db is empty`() = runTest {
        every { mockCredentialDao.readAll() } returns flowOf(emptyList())

        val result = service.readAll()
        val collectedResults = result.toList()

        verify(exactly = 1) { mockCredentialDao.readAll() }
        assertEquals(1, collectedResults.size)

        val actualCredentials = collectedResults.first()
        assertTrue (actualCredentials.isEmpty(), "Should return empty list when DB is empty")
    }

    @Test
    fun `delete should call dao delete (normal test)`() = runTest {
        val credential = Credential(1L, "test1@gmail.com", "test123")

        coEvery { mockCredentialDao.delete(credential.toEntity()) } just runs
        service.delete(credential)
        coVerify(exactly = 1) { mockCredentialDao.delete(credential.toEntity()) }
    }

    @Test
    fun `update should call dao update (normal test)`() = runTest {
        val credential = Credential(1L, "test1@gmail.com", "test123")

        coEvery { mockCredentialDao.update(credential.toEntity()) } just runs
        service.update(credential)
        coVerify(exactly = 1) { mockCredentialDao.update(credential.toEntity()) }
    }
}