package com.smsdndmanager.domain.usecase

import com.smsdndmanager.domain.model.SmsLogEntry
import com.smsdndmanager.domain.repository.SmsLogRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving SMS log entries
 */
class GetSmsLogsUseCase @Inject constructor(
    private val repository: SmsLogRepository
) {
    /**
     * Get all log entries
     */
    operator fun invoke(): Flow<List<SmsLogEntry>> = repository.getAllLogs()
    
    /**
     * Get logs within a specific time range
     */
    fun getLogsInRange(startTime: Long, endTime: Long): Flow<List<SmsLogEntry>> {
        return repository.getLogsInRange(startTime, endTime)
    }
}
