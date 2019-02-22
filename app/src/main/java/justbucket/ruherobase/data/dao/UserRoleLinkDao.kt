package justbucket.ruherobase.data.dao

import androidx.room.*
import justbucket.ruherobase.data.model.UserRoleLinkEntity

@Dao
interface UserRoleLinkDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUserAccessLink(userRoleLinkEntity: UserRoleLinkEntity): Long

    @Update
    suspend fun updateUserAccessLink(userRoleLinkEntity: UserRoleLinkEntity)

    @Delete
    suspend fun deleteUserAccessLink(userRoleLinkEntity: UserRoleLinkEntity)

    @Query("SELECT roleId FROM UserRoleLinkEntity WHERE roleId = :id")
    suspend fun getAllRoleLinks(id: Long): List<Long>

    @Query("DELETE FROM UserRoleLinkEntity WHERE roleId = :id")
    suspend fun deleteAllRoleLinks(id: Long)
}