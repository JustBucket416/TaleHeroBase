package justbucket.ruherobase.data.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import justbucket.ruherobase.data.model.HeroDescriptionEntity

@Dao
interface DescriptionDao {

    @Insert(onConflict = IGNORE)
    suspend fun insertDescription(heroDescriptionEntity: HeroDescriptionEntity)

    @Update
    suspend fun updateDescription(heroDescriptionEntity: HeroDescriptionEntity)

    @Delete
    suspend fun deleteDescription(heroDescriptionEntity: HeroDescriptionEntity)

    @Query("SELECT * FROM HeroDescriptionEntity WHERE heroId = :id")
    suspend fun findDescriptionByHeroId(id: Long): HeroDescriptionEntity
}