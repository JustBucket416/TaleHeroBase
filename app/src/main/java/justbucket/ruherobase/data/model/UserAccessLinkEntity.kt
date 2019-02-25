package justbucket.ruherobase.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index

@Entity(
        primaryKeys = ["userId", "accessTypeId"],
        foreignKeys = [
            ForeignKey(
                    entity = UserEntity::class,
                    parentColumns = ["userId"],
                    childColumns = ["userId"],
                    onDelete = CASCADE
            ),
            ForeignKey(
                    entity = AccessTypeEntity::class,
                    parentColumns = ["accessId"],
                    childColumns = ["accessTypeId"],
                    onDelete = CASCADE
            )],
        indices = [Index("userId"), Index("accessTypeId")]
)
data class UserAccessLinkEntity(

        val userId: Long,

        val accessTypeId: Long
)