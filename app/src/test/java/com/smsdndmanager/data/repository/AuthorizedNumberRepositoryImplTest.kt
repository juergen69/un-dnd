package com.smsdndmanager.data.repository

import com.smsdndmanager.data.local.EncryptedPreferencesDataSource
import com.smsdndmanager.domain.model.AuthorizedNumber
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class AuthorizedNumberRepositoryImplTest {

    @MockK
    private lateinit var dataSource: EncryptedPreferencesDataSource

    private lateinit var repository: AuthorizedNumberRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = AuthorizedNumberRepositoryImpl(dataSource)
    }

    @Test
    fun `normalizePhoneNumber should remove non-digit characters except plus`() {
        // Test cases
        assertEquals("+1234567890", repository.normalizePhoneNumber("+1 (234) 567-890"))
        assertEquals("1234567890", repository.normalizePhoneNumber("123-456-7890"))
        assertEquals("1234567890", repository.normalizePhoneNumber("(123) 456 7890"))
        assertEquals("+1234567890", repository.normalizePhoneNumber("  +1 234 567 890  "))
    }

    @Test
    fun `isAuthorized should return true for matching normalized number`() = runTest {
        // Given
        val entities = listOf(
            com.smsdndmanager.data.local.AuthorizedNumberEntity(
                "1", "+1234567890", "John", 1000L
            )
        )
        every { dataSource.getNumbers() } returns entities

        // When
        val result = repository.isAuthorized("123-456-7890")

        // Then
        assertTrue(result)
    }

    @Test
    fun `isAuthorized should return false for non-matching number`() = runTest {
        // Given
        val entities = listOf(
            com.smsdndmanager.data.local.AuthorizedNumberEntity(
                "1", "+1234567890", "John", 1000L
            )
        )
        every { dataSource.getNumbers() } returns entities

        // When
        val result = repository.isAuthorized("999-999-9999")

        // Then
        assertFalse(result)
    }

    @Test
    fun `addNumber should save to dataSource`() = runTest {
        // Given
        every { dataSource.getNumbers() } returns emptyList()
        coEvery { dataSource.saveNumbers(any()) } returns Unit

        val number = AuthorizedNumber("1", "+1234567890", "John", 1000L)

        // When
        val result = repository.addNumber(number)

        // Then
        assertTrue(result.isSuccess)
        coVerify { dataSource.saveNumbers(any()) }
    }

    @Test
    fun `removeNumber should delete from dataSource`() = runTest {
        // Given
        val entities = listOf(
            com.smsdndmanager.data.local.AuthorizedNumberEntity(
                "1", "+1234567890", "John", 1000L
            )
        )
        every { dataSource.getNumbers() } returns entities
        coEvery { dataSource.saveNumbers(any()) } returns Unit

        // When
        val result = repository.removeNumber("1")

        // Then
        assertTrue(result.isSuccess)
        coVerify { dataSource.saveNumbers(emptyList()) }
    }
}
