package justbucket.ruherobase.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    primaryKeys = ["userId", "accessTypeId"],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["userId"],
            childColumns = ["userId"]
        ),
        ForeignKey(
            entity = AccessTypeEntity::class,
            parentColumns = ["accessId"],
            childColumns = ["accessTypeId"]
        )],
    indices = [Index("userId"), Index("accessTypeId")]
)
data class UserAccessLinkEntity(

    val userId: Long,

    val accessTypeId: Long
)