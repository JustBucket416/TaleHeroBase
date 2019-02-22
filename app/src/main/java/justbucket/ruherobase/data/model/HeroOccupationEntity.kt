package justbucket.ruherobase.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = HeroEntity::class,
        parentColumns = ["heroId"],
        childColumns = ["heroId"]
    )],
    indices = [Index("heroId")]
)
data class HeroOccupationEntity(
    @PrimaryKey
    val occupationId: Long?,
    val heroId: Long,
    var occupation: String
)