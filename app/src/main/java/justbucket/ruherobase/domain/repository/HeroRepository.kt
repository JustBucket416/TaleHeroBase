package justbucket.ruherobase.domain.repository

import justbucket.ruherobase.domain.model.Hero

interface HeroRepository {

    suspend fun getAllEntities(): List<Hero>

    suspend fun addEntity(hero: Hero)

    suspend fun deleteEntity(hero: Hero)

    suspend fun updateEntity(hero: Hero)
}