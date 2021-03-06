package justbucket.ruherobase.data

import android.util.Log
import justbucket.ruherobase.data.database.HeroDatabase
import justbucket.ruherobase.data.mapper.mapAccessLongToDomain
import justbucket.ruherobase.data.mapper.mapToDomain
import justbucket.ruherobase.data.model.AccessTypeEntity
import justbucket.ruherobase.domain.model.LogEntry
import justbucket.ruherobase.domain.repository.LogEntryRepository
import java.util.*
import javax.inject.Inject

class LogEntryRepositoryImpl @Inject constructor(database: HeroDatabase) : LogEntryRepository {

    private val logDao = database.getLogDao()
    private val userDao = database.getUserDao()
    private val linkDao = database.getUserAccessLinkDao()

    override suspend fun getLogs(): List<LogEntry> {
        val logs = logDao.getAllLogs()
        return logs.map {
            val userAccessTypes = linkDao.getAllUserLinks(it.userId).map { AccessTypeEntity(it, "") }
            val user = userDao.getUserById(it.userId).mapToDomain(userAccessTypes.mapToDomain(), null)
            LogEntry(
                user,
                Date(it.dateMillis),
                mapAccessLongToDomain(it.accessTypeId),
                it.heroName
            )
        }
    }

    override suspend fun clearLogs() {
        logDao.clearLogs()
    }

    override suspend fun insertLog(entry: LogEntry) {

    }

    fun log(msg: String) = Log.w("THB", "[${Thread.currentThread().name}] $msg")
}