package justbucket.ruherobase.data.model

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = ["userTypeId", "accessTypeId"],
    foreignKeys = [
        ForeignKey(
            entity = UserTypeEntity::class,
            parentColumns = ["userTypeId"],
            childColumns = ["userTypeId"]
        ),
        ForeignKey(
            entity = AccessTypeEntity::class,
            parentColumns = ["accessId"],
            childColumns = ["accessTypeId"]
        )]
)
data class UserAccessLinkEntity(

    val userId: Long,

    val accessTypeId: Long
)