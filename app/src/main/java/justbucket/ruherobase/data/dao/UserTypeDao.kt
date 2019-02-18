package justbucket.ruherobase.data.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import justbucket.ruherobase.data.model.UserTypeEntity

@Dao
interface UserTypeDao {

    @Insert(onConflict = IGNORE)
    suspend fun insertUserType(userTypeEntity: UserTypeEntity)

    @Update
    suspend fun updateUserType(userTypeEntity: UserTypeEntity)

    @Delete
    suspend fun deleteUserType(userTypeEntity: UserTypeEntity)

    @Query("SELECT * FROM UserTypeEntity")
    suspend fun getAllUserTypes(): List<UserTypeEntity>

    @Query("SELECT * FROM UserTypeEntity WHERE userTypeName = :id")
    suspend fun getUserById(id: Long): UserTypeEntity
}