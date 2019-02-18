package justbucket.ruherobase.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity
data class HeroEntity(
    @PrimaryKey
    val heroId: Long?,
    var heroName: String,
    var heroPhotoUrl: String,
    var mentionedNumber: Int
)