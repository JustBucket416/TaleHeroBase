package justbucket.ruherobase.domain.model

import java.util.*

sealed class User(
    val id: Long,
    val name: String,
    val roles: List<Role>?,
    val accessTypeSet: EnumSet<AccessType>
)

class Admin(id: Long, name: String, roles: List<Role>? = null) : User(id, name, roles, EnumSet.allOf(AccessType::class.java))

class SimpleUser(id: Long, name: String, roles: List<Role>? = null) : User(id, name, roles, EnumSet.of(AccessType.READ))

class SuperUser(id: Long, name: String, roles: List<Role>? = null) : User(id, name, roles, EnumSet.of(AccessType.COPY, AccessType.READ))

class Moderator(id: Long, name: String, roles: List<Role>? = null) : User(id, name, roles, EnumSet.of(AccessType.UPDATE, AccessType.DELETE))