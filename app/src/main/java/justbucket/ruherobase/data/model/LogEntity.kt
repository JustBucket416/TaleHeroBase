package justbucket.ruherobase.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
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
        ),
        ForeignKey(
            entity = HeroEntity::class,
            parentColumns = ["heroId"],
            childColumns = ["heroId"]
        )]
)
data class LogEntity(
    @PrimaryKey
    val logId: Long,
    val userId: Long,
    val dateMillis: Long,
    val accessTypeId: Long,
    val heroId: Long
)