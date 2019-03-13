package justbucket.ruherobase.data.dao

import androidx.room.*
import justbucket.ruherobase.data.model.UserRoleLinkEntity

@Dao
interface UserRoleLinkDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUserRoleLink(userRoleLinkEntity: UserRoleLinkEntity): Long

    @Update
    suspend fun updateUserRoleLink(userRoleLinkEntity: UserRoleLinkEntity)

    @Delete
    suspend fun deleteUserRoleLink(userRoleLinkEntity: UserRoleLinkEntity)

    @Query("SELECT roleId FROM UserRoleLinkEntity WHERE userId = :id")
    suspend fun getAllRoleLinks(id: Long): List<Long>

    @Query("DELETE FROM UserRoleLinkEntity WHERE roleId = :id")
    suspend fun deleteAllRoleLinks(id: Long)
}