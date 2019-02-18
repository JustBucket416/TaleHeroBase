package justbucket.ruherobase.domain.repository

import justbucket.ruherobase.domain.model.LogEntry

interface LogRepository {

    suspend fun getLogs(): List<LogEntry>

    suspend fun clearLogs()

    suspend fun insertLog(entry: LogEntry)
}