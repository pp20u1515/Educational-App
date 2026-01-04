package com.example.programmingc.data.datasource.local.mapper

import com.example.databasedependencies.entity.UserEntity
import com.example.programmingc.domain.model.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class UserMapperTest {
    @Test
    fun `toDomain should map all fields correctly`(){
        val date = System.currentTimeMillis()
        val entity = UserEntity(
            id = "1",
            email = "test@gmail.com",
            password = "test123",
            registrationDate = date
        )
        val result = entity.toDomain()

        assertEquals(entity.id, result.id)
        assertEquals(entity.email, result.email)
        assertEquals(entity.password, result.password)
        assertEquals(entity.registrationDate, result.registrationDate)
    }

    @Test
    fun `toDomain with empty string`(){
        val date = System.currentTimeMillis()
        val entity = UserEntity(
            id = "2",
            email = "",
            password = " ",
            registrationDate = date
        )
        val result = entity.toDomain()

        assertEquals(entity.id, result.id)
        assertEquals("", result.email)
        assertEquals(entity.password, result.password)
        assertEquals(entity.registrationDate, result.registrationDate)
    }

    @Test
    fun `toEntity should map all fields correctly`(){
        val date = System.currentTimeMillis()
        val domain = User(
            id = "1",
            email = "test@gmail.com",
            password = "test123",
            registrationDate = date
        )
        val result = domain.toEntity()

        assertEquals(domain.id, result.id)
        assertEquals(domain.email, result.email)
        assertEquals(domain.password, result.password)
        assertEquals(domain.registrationDate, result.registrationDate)
    }

    @Test
    fun `toEntity with empty strings`(){
        val date = System.currentTimeMillis()
        val domain = User(
            id = "3",
            email = "",
            password = " ",
            registrationDate = date
        )
        val result = domain.toEntity()

        assertEquals(domain.id, result.id)
        assertEquals(domain.email, result.email)
        assertEquals(domain.password, result.password)
        assertEquals(domain.registrationDate, result.registrationDate)
    }

    @Test
    fun `toEntity should preserve data integrity`(){
        val originalEntity = UserEntity(
            id = "5",
            email = "new_test@gmail.com",
            password = "test me"
        )
        val domain = originalEntity.toDomain()
        val result = domain.toEntity()

        assertEquals(originalEntity.id, result.id)
        assertEquals(originalEntity.email, result.email)
        assertEquals(originalEntity.password, result.password)
        assertEquals(originalEntity.registrationDate, result.registrationDate)
    }
}