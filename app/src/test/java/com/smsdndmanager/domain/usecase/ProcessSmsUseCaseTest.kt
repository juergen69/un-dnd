package com.smsdndmanager.domain.usecase

import android.content.Context
import android.media.AudioManager
import com.smsdndmanager.domain.model.SmsMessage
import com.smsdndmanager.domain.repository.AuthorizedNumberRepository
import com.smsdndmanager.domain.repository.SettingsRepository
import com.smsdndmanager.domain.repository.SmsLogRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class ProcessSmsUseCaseTest {

    @MockK
    private lateinit var authorizedNumberRepository: AuthorizedNumberRepository

    @MockK
    private lateinit var smsLogRepository: SmsLogRepository

    @MockK
    private lateinit var settingsRepository: SettingsRepository

    @MockK
    private lateinit var context: Context

    @MockK
    private lateinit var audioManager: AudioManager

    private lateinit var useCase: ProcessSmsUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        every { context.getSystemService(Context.AUDIO_SERVICE) } returns audioManager
        every { audioManager.getStreamMaxVolume(AudioManager.STREAM_RING) } returns 15
        every { audioManager.ringerMode = any() } returns Unit
        every { audioManager.setStreamVolume(any(), any(), any()) } returns Unit
        
        coEvery { settingsRepository.shouldSendConfirmation() } returns flowOf(false)
        coEvery { settingsRepository.getActivationPattern() } returns flowOf("undnd")
        coEvery { smsLogRepository.addLog(any()) } returns Result.success(Unit)

        useCase = ProcessSmsUseCase(
            authorizedNumberRepository,
            smsLogRepository,
            settingsRepository,
            context
        )
    }

    @Test
    fun `parseCommand should extract volume from valid undnd command`() = runTest {
        // Given
        coEvery { settingsRepository.getActivationPattern() } returns flowOf("undnd")
        
        // Test various valid commands
        assertEquals(50, useCase.parseCommand("undnd50")?.percentage)
        assertEquals(100, useCase.parseCommand("undnd100")?.percentage)
        assertEquals(0, useCase.parseCommand("undnd0")?.percentage)
        assertEquals(75, useCase.parseCommand("Hello undnd75 world")?.percentage)
    }

    @Test
    fun `parseCommand should handle invalid commands`() = runTest {
        // Given
        coEvery { settingsRepository.getActivationPattern() } returns flowOf("undnd")
        
        assertNull(useCase.parseCommand("hello"))
        assertNull(useCase.parseCommand("undnd"))
        assertNull(useCase.parseCommand("undndabc"))
        assertNull(useCase.parseCommand("undnd101")) // Out of range
        assertNull(useCase.parseCommand(""))
    }

    @Test
    fun `parseCommand should be case insensitive`() = runTest {
        // Given
        coEvery { settingsRepository.getActivationPattern() } returns flowOf("undnd")
        
        assertEquals(50, useCase.parseCommand("UNDND50")?.percentage)
        assertEquals(50, useCase.parseCommand("UndNd50")?.percentage)
    }

    @Test
    fun `invoke should ignore message from unauthorized number`() = runTest {
        // Given
        val smsMessage = SmsMessage(
            senderNumber = "+1234567890",
            body = "undnd50",
            timestamp = System.currentTimeMillis()
        )
        coEvery { authorizedNumberRepository.normalizePhoneNumber(any()) } returns "+1234567890"
        coEvery { authorizedNumberRepository.isAuthorized(any()) } returns false

        // When
        val result = useCase(smsMessage)

        // Then
        assertTrue(result.isSuccess)
        val processResult = result.getOrNull()
        assertTrue(processResult is ProcessSmsUseCase.ProcessResult.Ignored)
    }

    @Test
    fun `invoke should process valid command from authorized number`() = runTest {
        // Given
        val senderNumber = "+1234567890"
        val smsMessage = SmsMessage(
            senderNumber = senderNumber,
            body = "undnd50",
            timestamp = System.currentTimeMillis()
        )
        coEvery { authorizedNumberRepository.normalizePhoneNumber(any()) } returns senderNumber
        coEvery { authorizedNumberRepository.isAuthorized(any()) } returns true

        // When
        val result = useCase(smsMessage)

        // Then
        assertTrue(result.isSuccess)
        val processResult = result.getOrNull()
        assertTrue(processResult is ProcessSmsUseCase.ProcessResult.Success)
        assertEquals(50, (processResult as ProcessSmsUseCase.ProcessResult.Success).volumeSet)
        
        // Verify audio manager was called
        verify { audioManager.setStreamVolume(AudioManager.STREAM_RING, 7, 0) } // 50% of 15 = 7.5 -> 7
    }

    @Test
    fun `invoke should log successful execution`() = runTest {
        // Given
        val smsMessage = SmsMessage(
            senderNumber = "+1234567890",
            body = "undnd75",
            timestamp = System.currentTimeMillis()
        )
        coEvery { authorizedNumberRepository.normalizePhoneNumber(any()) } returns "+1234567890"
        coEvery { authorizedNumberRepository.isAuthorized(any()) } returns true

        // When
        useCase(smsMessage)

        // Then
        coVerify { smsLogRepository.addLog(any()) }
    }
}
