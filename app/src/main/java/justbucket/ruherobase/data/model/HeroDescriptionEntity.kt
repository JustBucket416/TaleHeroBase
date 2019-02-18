package justbucket.ruherobase.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = HeroEntity::class,
        parentColumns = ["heroId"],
        childColumns = ["heroId"]
    )]
)
data class HeroDescriptionEntity(
    @PrimaryKey
    val descId: Long?,
    val heroId: Long,
    var description: String
)