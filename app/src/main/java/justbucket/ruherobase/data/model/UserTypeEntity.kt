package justbucket.ruherobase.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.NO_ACTION
import androidx.room.PrimaryKey
import justbucket.ruherobase.data.model.AccessTypeEntity

@Entity
data class UserTypeEntity(
    @PrimaryKey
    val userTypeId: Long,
    val userTypeName: String
)