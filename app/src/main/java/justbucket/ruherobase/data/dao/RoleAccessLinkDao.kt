package justbucket.ruherobase.data.dao

import androidx.room.*
import justbucket.ruherobase.data.model.RoleAccessLinkEntity

@Dao
interface RoleAccessLinkDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRoleAccessLink(roleAccessLinkEntity: RoleAccessLinkEntity): Long

    @Update
    suspend fun updateRoleAccessLink(roleAccessLinkEntity: RoleAccessLinkEntity)

    @Delete
    suspend fun deleteRoleAccessLink(roleAccessLinkEntity: RoleAccessLinkEntity)

    @Query("SELECT accessId FROM RoleAccessLinkEntity WHERE roleId = :id")
    suspend fun getAllRoleLinks(id: Long): List<Long>

    @Query("DELETE FROM RoleAccessLinkEntity WHERE roleId = :id")
    suspend fun deleteAllRoleLinks(id: Long)
}