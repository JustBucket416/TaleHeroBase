package justbucket.ruherobase.data

import justbucket.ruherobase.data.database.HeroDatabase
import justbucket.ruherobase.data.mapper.mapToDomain
import justbucket.ruherobase.data.model.HeroDescriptionEntity
import justbucket.ruherobase.data.model.HeroEntity
import justbucket.ruherobase.data.model.HeroOccupationEntity
import justbucket.ruherobase.domain.model.Hero
import justbucket.ruherobase.domain.repository.HeroRepository

class HeroRepositoryImpl(database: HeroDatabase) : HeroRepository {

    private val heroDao = database.getHeroDao()
    private val occDao = database.getOccupationDao()
    private val descDao = database.getDescriptionDao()

    override suspend fun getAllEntities(): List<Hero> {
        val heroes = heroDao.getAllheroes()
        return heroes.map {
            val descriptionEntity = descDao.findDescriptionByHeroId(it.heroId!!)
            val occupationEntity = occDao.findOccupationByHeroId(it.heroId)
            Triple(it, occupationEntity, descriptionEntity).mapToDomain()
        }
    }

    override suspend fun addEntity(hero: Hero) {
        val heroEntity = HeroEntity(null, hero.name, hero.photoUrl, hero.mentionNumber)
        val heroId = heroDao.insertHero(heroEntity)
        occDao.insertOccupation(HeroOccupationEntity(null, heroId, hero.occupation))
        descDao.insertDescription(HeroDescriptionEntity(null, heroId, hero.description))
    }

    override suspend fun deleteEntity(hero: Hero) {
        heroDao.deleteHeroById(hero.id)
    }

    override suspend fun updateEntity(hero: Hero) {
        val occupationEntity = occDao.findOccupationByHeroId(hero.id)
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
    }
}