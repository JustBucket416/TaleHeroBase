package justbucket.ruherobase.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RoleEntity(
    @PrimaryKey
    val roleId: Long,
    val roleName: String
)