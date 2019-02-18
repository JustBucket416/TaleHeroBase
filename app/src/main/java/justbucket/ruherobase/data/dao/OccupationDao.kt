package justbucket.ruherobase.data.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import justbucket.ruherobase.data.model.HeroOccupationEntity

@Dao
interface OccupationDao {

    @Insert(onConflict = IGNORE)
    suspend fun insertOccupation(heroOccupationEntity: HeroOccupationEntity)

    @Update
    suspend fun updateOccupation(heroOccupationEntity: HeroOccupationEntity)

    @Delete
    suspend fun deleteOccupation(heroOccupationEntity: HeroOccupationEntity)

    @Query("SELECT * FROM HeroOccupationEntity WHERE heroId = :id")
    suspend fun findOccupationByHeroId(id: Long): HeroOccupationEntity

}