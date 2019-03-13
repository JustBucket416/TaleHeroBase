package justbucket.ruherobase.data

import justbucket.ruherobase.data.database.HeroDatabase
import justbucket.ruherobase.data.mapper.mapToDomain
import justbucket.ruherobase.data.model.HeroDescriptionEntity
import justbucket.ruherobase.data.model.HeroEntity
import justbucket.ruherobase.data.model.HeroOccupationEntity
import justbucket.ruherobase.data.model.LogEntity
import justbucket.ruherobase.domain.model.Hero
import justbucket.ruherobase.domain.repository.HeroRepository
import javax.inject.Inject

class HeroRepositoryImpl @Inject constructor(database: HeroDatabase) : HeroRepository {

    private val heroDao = database.getHeroDao()
    private val occDao = database.getOccupationDao()
    private val descDao = database.getDescriptionDao()
    private val logDao = database.getLogDao()

    override suspend fun getAllHeroes(): List<Hero> {
        val heroes = heroDao.getAllHeroes()
        //logDao.insertLog(LogEntity(null, userId, System.currentTimeMillis(), 2L, -1))
        return heroes.map {
            val descriptionEntity = descDao.findDescriptionByHeroId(it.heroId!!)
            val occupationEntity = occDao.findOccupationByHeroId(it.heroId)
            Triple(it, occupationEntity, descriptionEntity).mapToDomain()
        }
    }

    override suspend fun addHero(hero: Hero, userId: Long) {
        val heroEntity = HeroEntity(null, hero.name, hero.photoUrl, hero.mentionNumber)
        val heroId = heroDao.insertHero(heroEntity)
        occDao.insertOccupation(HeroOccupationEntity(null, heroId, hero.occupation))
        descDao.insertDescription(HeroDescriptionEntity(null, heroId, hero.description))
        logDao.insertLog(LogEntity(null, userId, System.currentTimeMillis(), 0L, hero.name))
    }

    override suspend fun deleteHero(hero: Hero, userId: Long) {
        descDao.deleteDescription(descDao.findDescriptionByHeroId(hero.id!!))
        occDao.deleteOccupation(occDao.findOccupationByHeroId(hero.id))
        heroDao.deleteHeroById(hero.id)
        logDao.insertLog(LogEntity(null, userId, System.currentTimeMillis(), 3L, hero.name))
    }

    override suspend fun updateHero(hero: Hero, userId: Long) {
        val occupationEntity = occDao.findOccupationByHeroId(hero.id!!)
        occupationEntity.occupation = hero.occupation
        occDao.updateOccupation(occupationEntity)

        val descriptionEntity = descDao.findDescriptionByHeroId(hero.id)
        descriptionEntity.description = hero.description
        descDao.updateDescription(descriptionEntity)

        val heroEntity = heroDao.findHeroById(hero.id)
        heroEntity.heroName = hero.name
        heroEntity.heroPhotoUrl = hero.photoUrl
        heroEntity.mentionedNumber = hero.mentionNumber
        heroDao.updateHero(heroEntity)
        logDao.insertLog(LogEntity(null, userId, System.currentTimeMillis(), 2L, hero.name))
    }
}