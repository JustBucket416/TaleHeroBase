package justbucket.ruherobase.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
        foreignKeys = [ForeignKey(
                entity = HeroEntity::class,
                parentColumns = ["heroId"],
                childColumns = ["heroId"],
                onDelete = CASCADE
        )],
        indices = [Index("heroId")]
)
data class HeroDescriptionEntity(
        @PrimaryKey
        val descId: Long?,
        val heroId: Long?,
        var description: String
)