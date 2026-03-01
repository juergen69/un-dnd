package com.smsdndmanager.domain.usecase

import com.smsdndmanager.domain.model.AuthorizedNumber
import com.smsdndmanager.domain.repository.AuthorizedNumberRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ManageAuthorizedNumbersUseCaseTest {

    @MockK
    private lateinit var repository: AuthorizedNumberRepository

    private lateinit var useCase: ManageAuthorizedNumbersUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = ManageAuthorizedNumbersUseCase(repository)
    }

    @Test
    fun `getAllNumbers should return flow from repository`() = runTest {
        // Given
        val numbers = listOf(
            AuthorizedNumber("1", "+1234567890", "John", 1000L),
            AuthorizedNumber("2", "+0987654321", "Jane", 2000L)
        )
        coEvery { repository.getAllNumbers() } returns flowOf(numbers)

        // When
        val result = useCase.getAllNumbers().first()

        // Then
        assertEquals(2, result.size)
        assertEquals("John", result[0].displayName)
    }

    @Test
    fun `addNumber should return error for empty phone number`() = runTest {
        // When
        val result = useCase.addNumber("", "Name")

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `addNumber should return error for duplicate number`() = runTest {
        // Given
        coEvery { repository.normalizePhoneNumber(any()) } returns "+1234567890"
        coEvery { repository.isAuthorized(any()) } returns true

        // When
        val result = useCase.addNumber("+1234567890", "John")

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `addNumber should succeed for new valid number`() = runTest {
        // Given
        coEvery { repository.normalizePhoneNumber(any()) } returns "+1234567890"
        coEvery { repository.isAuthorized(any()) } returns false
        coEvery { repository.addNumber(any()) } returns Result.success(Unit)

        // When
        val result = useCase.addNumber("+1234567890", "John")

        // Then
        assertTrue(result.isSuccess)
        coVerify { repository.addNumber(any()) }
    }

    @Test
    fun `removeNumber should call repository`() = runTest {
        // Given
        coEvery { repository.removeNumber(any()) } returns Result.success(Unit)

        // When
        val result = useCase.removeNumber("1")

        // Then
        assertTrue(result.isSuccess)
        coVerify { repository.removeNumber("1") }
    }

    @Test
    fun `isAuthorized should normalize and check repository`() = runTest {
        // Given
        coEvery { repository.normalizePhoneNumber(any()) } returns "+1234567890"
        coEvery { repository.isAuthorized(any()) } returns true

        // When
        val result = useCase.isAuthorized("123-456-7890")

        // Then
        assertTrue(result)
    }
}
