package justbucket.ruherobase.data.dao

import androidx.room.*
import justbucket.ruherobase.data.model.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(userEntity: UserEntity): Long

    @Update
    suspend fun updateUser(userEntity: UserEntity)

    @Delete
    suspend fun deleteUser(userEntity: UserEntity)

    @Query("SELECT * FROM UserEntity")
    suspend fun getAllUsers(): List<UserEntity>

    @Query("SELECT * FROM UserEntity WHERE userId = :id")
    suspend fun getUserById(id: Long): UserEntity
}