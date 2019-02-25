package justbucket.ruherobase.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index("heroName", unique = true)])
data class HeroEntity(
        @PrimaryKey
        val heroId: Long?,
        var heroName: String,
        var heroPhotoUrl: String,
        var mentionedNumber: Int
)