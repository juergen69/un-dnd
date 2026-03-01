package com.smsdndmanager.domain.repository

import com.smsdndmanager.domain.model.SmsLogEntry
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for SMS log entries
 */
interface SmsLogRepository {
    
    /**
     * Get all log entries as a Flow
     */
    fun getAllLogs(): Flow<List<SmsLogEntry>>
    
    /**
     * Add a new log entry
     */
    suspend fun addLog(entry: SmsLogEntry): Result<Unit>
    
    /**
     * Clear all logs
     */
    suspend fun clearLogs(): Result<Unit>
    
    /**
     * Get logs within a time range
     */
    fun getLogsInRange(startTime: Long, endTime: Long): Flow<List<SmsLogEntry>>
}
