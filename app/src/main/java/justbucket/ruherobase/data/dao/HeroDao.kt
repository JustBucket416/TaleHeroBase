package justbucket.ruherobase.data.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import justbucket.ruherobase.data.model.HeroEntity

@Dao
interface HeroDao {

    @Insert(onConflict = IGNORE)
    suspend fun insertHero(heroEntity: HeroEntity): Long

    @Update
    suspend fun updateHero(heroEntity: HeroEntity)

    @Delete
    suspend fun deleteHero(heroEntity: HeroEntity)

    @Query("SELECT * FROM HeroEntity")
    suspend fun getAllHeroes(): List<HeroEntity>

    @Query("DELETE FROM HeroEntity WHERE heroId = :id")
    suspend fun deleteHeroById(id: Long)

    @Query("SELECT * FROM HeroEntity WHERE heroId = :id")
    suspend fun findHeroById(id: Long): HeroEntity
}