package justbucket.ruherobase.data.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import justbucket.ruherobase.data.model.LogEntity

@Dao
interface LogDao {

    @Insert(onConflict = IGNORE)
    suspend fun insertLog(logEntity: LogEntity)

    @Update
    suspend fun updateLog(logEntity: LogEntity)

    @Delete
    suspend fun deleteLog(logEntity: LogEntity)

    @Query("SELECT * FROM LogEntity ORDER BY dateMillis ASC")
    suspend fun getAllLogs(): List<LogEntity>

    @Query("DELETE FROM LogEntity")
    suspend fun clearLogs()
}