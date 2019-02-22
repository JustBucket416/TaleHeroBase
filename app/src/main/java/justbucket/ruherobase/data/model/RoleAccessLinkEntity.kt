package justbucket.ruherobase.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    primaryKeys = ["accessId", "roleId"],
    foreignKeys = [
        ForeignKey(
            entity = AccessTypeEntity::class,
            parentColumns = ["accessId"],
            childColumns = ["accessId"]
        ),
        ForeignKey(
            entity = RoleEntity::class,
            parentColumns = ["roleId"],
            childColumns = ["roleId"]
        )],
    indices = [Index("accessId"), Index("roleId")]
)
data class RoleAccessLinkEntity(
    val accessId: Long,
    val roleId: Long
)