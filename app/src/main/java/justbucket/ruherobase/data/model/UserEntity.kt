package justbucket.ruherobase.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = UserTypeEntity::class,
            parentColumns = ["userTypeId"],
            childColumns = ["userTypeId"]
        )],
    indices = [Index("userTypeId"), Index("userName", unique = true)]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val userId: Long?,
    val userTypeId: Long,
    val userName: String
)