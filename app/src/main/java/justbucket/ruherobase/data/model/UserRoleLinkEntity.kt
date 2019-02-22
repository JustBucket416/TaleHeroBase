package justbucket.ruherobase.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    primaryKeys = ["userId", "roleId"],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["userId"],
            childColumns = ["userId"]
        ),
        ForeignKey(
            entity = RoleEntity::class,
            parentColumns = ["roleId"],
            childColumns = ["roleId"]
        )],
    indices = [Index("userId"), Index("roleId")]
)
data class UserRoleLinkEntity(
    val userId: Long,
    val roleId: Long
)