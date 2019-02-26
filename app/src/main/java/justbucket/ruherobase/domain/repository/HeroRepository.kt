package justbucket.ruherobase.domain.repository

import justbucket.ruherobase.domain.model.Hero

interface HeroRepository {

    suspend fun getAllHeroes(): List<Hero>

    suspend fun addHero(hero: Hero)

    suspend fun deleteHero(hero: Hero)

    suspend fun updateHero(hero: Hero)
}