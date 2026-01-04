package com.example.programmingc.data.datasource.local.mapper

import com.example.databasedependencies.entity.CredentialEntity
import com.example.programmingc.domain.model.Credential
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class CredentialMapperTest {
    @Test
    fun `toDomain should map all fields correctly`(){
        val entity = CredentialEntity(
            id = 1L,
            email = "test@gmail.com",
            password = "test123"
        )
        val result = entity.toDomain()

        assertEquals(1L,result.id)
        assertEquals("test@gmail.com", result.email)
        assertEquals("test123", result.password)
    }

    @Test
    fun `toDomain with empty strings`(){
        val entity = CredentialEntity(
            id = 2L,
            email = "",
            password = ""
        )
        val result = entity.toDomain()

        assertEquals(2L, result.id)
        assertEquals("", result.email)
        assertEquals("", result.password)
    }

    @Test
    fun `toEntity should map all fields correctly`(){
        val domain = Credential(
            id = 1L,
            email = "test@yandex.ru",
            password = "test12@"
        )
        val result = domain.toEntity()

        assertEquals(1L, result.id)
        assertEquals("test@yandex.ru", result.email)
        assertEquals("test12@", result.password)
    }

    @Test
    fun `toEntity with empty string`(){
        val domain = Credential(
            id = 2L,
            email = "",
            password = ""
        )
        val result = domain.toEntity()

        assertEquals(2L, result.id)
        assertEquals("", result.email)
        assertEquals("", result.password)
    }

    @Test
    fun `toEntity should preserve data integrity`(){
        val originalEntity = CredentialEntity(
            id = 5L,
            email = "testEmail@gmail.com",
            password = "test123!"
        )

        val domain = originalEntity.toDomain()
        val result = domain.toEntity()

        assertEquals(originalEntity.id, result.id)
        assertEquals(originalEntity.email, result.email)
        assertEquals(originalEntity.password, result.password)
    }
}