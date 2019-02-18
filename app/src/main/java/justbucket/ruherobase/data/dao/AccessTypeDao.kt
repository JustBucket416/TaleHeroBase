package justbucket.ruherobase.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import justbucket.ruherobase.data.model.AccessTypeEntity

@Dao
interface AccessTypeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAccessType(accessTypeEntity: AccessTypeEntity)
}