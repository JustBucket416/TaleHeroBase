package justbucket.ruherobase.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = UserTypeEntity::class,
            parentColumns = ["userTypeId"],
            childColumns = ["userTypeId"],
            onDelete = ForeignKey.NO_ACTION
        )]
)
data class UserEntity(
    @PrimaryKey
    val userId: Long?,
    val userTypeId: Long,
    val userName: String
)