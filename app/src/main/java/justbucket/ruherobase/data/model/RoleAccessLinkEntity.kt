package justbucket.ruherobase.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index

@Entity(
        primaryKeys = ["accessId", "roleId"],
        foreignKeys = [
            ForeignKey(
                    entity = AccessTypeEntity::class,
                    parentColumns = ["accessId"],
                    childColumns = ["accessId"],
                    onDelete = CASCADE
            ),
            ForeignKey(
                    entity = RoleEntity::class,
                    parentColumns = ["roleId"],
                    childColumns = ["roleId"],
                    onDelete = CASCADE
            )],
        indices = [Index("accessId"), Index("roleId")]
)
data class RoleAccessLinkEntity(
        val accessId: Long,
        val roleId: Long
)