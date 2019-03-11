package justbucket.ruherobase.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Role(
        val id: Long?,
        val accessTypes: EnumSet<AccessType>,
        val roleName: String
) : Parcelable