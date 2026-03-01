package com.smsdndmanager.data.repository

import com.smsdndmanager.data.local.EncryptedPreferencesDataSource
import com.smsdndmanager.domain.model.SmsLogEntry
import com.smsdndmanager.domain.repository.SmsLogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of SmsLogRepository using encrypted preferences
 */
@Singleton
class SmsLogRepositoryImpl @Inject constructor(
    private val dataSource: EncryptedPreferencesDataSource
) : SmsLogRepository {

    companion object {
        private const val MAX_LOG_ENTRIES = 100 // Limit to prevent unlimited growth
    }

    override fun getAllLogs(): Flow<List<SmsLogEntry>> {
        return dataSource.getLogsFlow().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun addLog(entry: SmsLogEntry): Result<Unit> {
        return try {
            val currentLogs = dataSource.getLogsFlow().value.map { it.toDomainModel() }.toMutableList()
            currentLogs.add(0, entry) // Add to beginning (newest first)
            
            // Keep only the most recent entries
            if (currentLogs.size > MAX_LOG_ENTRIES) {
                currentLogs.subList(MAX_LOG_ENTRIES, currentLogs.size).clear()
            }
            
            dataSource.saveLogs(currentLogs.map { it.toEntity() })
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun clearLogs(): Result<Unit> {
        return try {
            dataSource.saveLogs(emptyList())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getLogsInRange(startTime: Long, endTime: Long): Flow<List<SmsLogEntry>> {
        return dataSource.getLogsFlow().map { entities ->
            entities
                .filter { it.timestamp in startTime..endTime }
                .map { it.toDomainModel() }
        }
    }

    // Mapping functions
    private fun com.smsdndmanager.data.local.SmsLogEntity.toDomainModel(): SmsLogEntry {
        return SmsLogEntry(
            id = id,
            senderNumber = senderNumber,
            command = command,
            volumeSet = volumeSet,
            timestamp = timestamp,
            success = success
        )
    }

    private fun SmsLogEntry.toEntity(): com.smsdndmanager.data.local.SmsLogEntity {
        return com.smsdndmanager.data.local.SmsLogEntity(
            id = id,
            senderNumber = senderNumber,
            command = command,
            volumeSet = volumeSet,
            timestamp = timestamp,
            success = success
        )
    }
}
