package justbucket.ruherobase.data.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import justbucket.ruherobase.data.model.RoleEntity

@Dao
interface RoleDao {

    @Insert(onConflict = IGNORE)
    suspend fun insertRole(rolEntity: RoleEntity)

    @Update
    suspend fun updateRole(roleEntity: RoleEntity)

    @Delete
    suspend fun deleteRole(roleEntity: RoleEntity)

    @Query("SELECT * FROM RoleEntity")
    suspend fun getAllRoles(): List<RoleEntity>

    @Query("SELECT * FROM RoleEntity WHERE roleId = :id")
    suspend fun getRoleById(id: Long): RoleEntity

}