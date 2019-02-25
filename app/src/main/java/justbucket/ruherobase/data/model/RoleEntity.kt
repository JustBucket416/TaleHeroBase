package justbucket.ruherobase.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index("roleName", unique = true)])
data class RoleEntity(
    @PrimaryKey
    val roleId: Long?,
    val roleName: String
)