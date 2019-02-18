package justbucket.ruherobase.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.NO_ACTION
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = AccessTypeEntity::class,
            parentColumns = ["accessId"],
            childColumns = ["roleId"],
            onDelete = NO_ACTION
        )
    ]
)
data class RoleEntity(
    @PrimaryKey
    val roleId: Long,
    val accessTypeId: Long,
    val roleName: String
)