package justbucket.ruherobase.data.model

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = AccessTypeEntity::class,
            parentColumns = ["accessId"],
            childColumns = ["accessId"]
        ),
        ForeignKey(
            entity = RoleEntity::class,
            parentColumns = ["roleId"],
            childColumns = ["roledId"]
        )]
)
data class RoleAccessLinkEntity(
    val accessId: Long,
    val roleId: Long
)