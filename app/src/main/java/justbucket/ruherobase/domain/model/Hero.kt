package justbucket.ruherobase.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Hero(
    val id: Long?,
    val name: String,
    val mentionNumber: Int,
    val description: String,
    val occupation: String,
    val photoUrl: String
) : Parcelable