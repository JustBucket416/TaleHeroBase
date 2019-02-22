package justbucket.ruherobase.data.dao

import androidx.room.*
import justbucket.ruherobase.data.model.UserAccessLinkEntity

@Dao
interface UserAccessLinkDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUserAccessLink(UserAccessLinkEntity: UserAccessLinkEntity): Long

    @Update
    suspend fun updateUserAccessLink(UserAccessLinkEntity: UserAccessLinkEntity)

    @Delete
    suspend fun deleteUserAccessLink(UserAccessLinkEntity: UserAccessLinkEntity)

    @Query("SELECT accessTypeId FROM UserAccessLinkEntity WHERE userId = :id")
    suspend fun getAllUserLinks(id: Long): List<Long>

    @Query("DELETE FROM UserAccessLinkEntity WHERE userId = :id")
    suspend fun deleteAllUserLinks(id: Long)
}