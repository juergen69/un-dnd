package com.smsdndmanager.domain.usecase

import com.smsdndmanager.domain.repository.SmsLogRepository
import javax.inject.Inject

/**
 * Use case for clearing SMS logs
 */
class ClearLogsUseCase @Inject constructor(
    private val repository: SmsLogRepository
) {
    /**
     * Clear all log entries
     */
    suspend operator fun invoke(): Result<Unit> {
        return repository.clearLogs()
    }
}
