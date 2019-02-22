package justbucket.ruherobase.data

import justbucket.ruherobase.data.database.HeroDatabase
import justbucket.ruherobase.data.mapper.mapAccessLongToDomain
import justbucket.ruherobase.data.mapper.mapToDomain
import justbucket.ruherobase.data.model.AccessTypeEntity
import justbucket.ruherobase.domain.model.LogEntry
import justbucket.ruherobase.domain.repository.LogEntryRepository
import java.util.*

class LogEntryRepositoryImpl(database: HeroDatabase) : LogEntryRepository {

    private val logDao = database.getLogDao()
    private val userDao = database.getUserDao()
    private val heroDao = database.getHeroDao()
    private val descDao = database.getDescriptionDao()
    private val occDao = database.getOccupationDao()
    private val linkDao = database.getUserAccessLinkDao()

    override suspend fun getLogs(): List<LogEntry> {
        val logs = logDao.getAllLogs()
        return logs.map {
            val heroEntity = heroDao.findHeroById(it.heroId)
            val occupationEntity = occDao.findOccupationByHeroId(heroEntity.heroId!!)
            val descriptionEntity = descDao.findDescriptionByHeroId(heroEntity.heroId)
            val hero = Triple(heroEntity, occupationEntity, descriptionEntity).mapToDomain()

            val userAccessTypes = linkDao.getAllUserLinks(it.userId).map { AccessTypeEntity(it, "") }
            val user = userDao.getUserById(it.userId).mapToDomain(userAccessTypes, null)
            LogEntry(user, Date(it.dateMillis), mapAccessLongToDomain(it.accessTypeId), hero)
        }
    }

    override suspend fun clearLogs() {

    }

    override suspend fun insertLog(entry: LogEntry) {

    }
}