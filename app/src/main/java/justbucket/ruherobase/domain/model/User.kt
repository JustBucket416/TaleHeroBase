package justbucket.ruherobase.domain.model

import java.util.*

sealed class User(
    val id: Long,
    var name: String,
    var role: Role?,
    val accessTypeSet: EnumSet<AccessType>
)

class Admin(id: Long, name: String) : User(id, name, null, EnumSet.allOf(AccessType::class.java))

class SimpleUser(id: Long, name: String) : User(id, name, null, EnumSet.of(AccessType.READ))

class SuperUser(id: Long, name: String) : User(id, name, null, EnumSet.of(AccessType.COPY, AccessType.READ))

class Moderator(id: Long, name: String) : User(id, name, null, EnumSet.of(AccessType.UPDATE, AccessType.DELETE))