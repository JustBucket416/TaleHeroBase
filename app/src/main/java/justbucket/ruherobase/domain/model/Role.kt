package justbucket.ruherobase.domain.model

import java.util.*

data class Role(
    val id: Long?,
    val accessTypes: EnumSet<AccessType>,
    val roleName: String
)