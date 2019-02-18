package justbucket.ruherobase.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AccessTypeEntity(
    @PrimaryKey
    val accessId: Long,
    val description: String
)