package justbucket.ruherobase.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
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
        ),
        ForeignKey(
            entity = HeroEntity::class,
            parentColumns = ["heroId"],
            childColumns = ["heroId"]
        )],
    indices = [Index("accessTypeId"), Index("userId"), Index("heroId")]
)
data class LogEntity(
    @PrimaryKey
    val logId: Long,
    val userId: Long,
    val dateMillis: Long,
    val accessTypeId: Long,
    val heroId: Long
)