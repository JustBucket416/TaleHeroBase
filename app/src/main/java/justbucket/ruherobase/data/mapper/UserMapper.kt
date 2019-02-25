package justbucket.ruherobase.data.mapper

import justbucket.ruherobase.data.model.UserEntity
import justbucket.ruherobase.domain.model.*
import java.util.*

fun User.mapToData() = when (this) {
    is Admin -> Triple(UserEntity(id, 0, name), accessTypeSet.mapToData(), roles?.map { it.mapToData() })
    is SimpleUser -> Triple(UserEntity(id, 1, name), accessTypeSet.mapToData(), roles?.map { it.mapToData() })
    is SuperUser -> Triple(UserEntity(id, 2, name), accessTypeSet.mapToData(), roles?.map { it.mapToData() })
    is Moderator -> Triple(UserEntity(id, 3, name), accessTypeSet.mapToData(), roles?.map { it.mapToData() })
}

fun UserEntity.mapToDomain(accessEntities: List<AccessType>, roles: List<Role>?) =
    when (accessEntities) {
        EnumSet.allOf(AccessType::class.java).toList() -> Admin(userTypeId, userName, roles)
        EnumSet.of(AccessType.READ).toList() -> SimpleUser(userTypeId, userName, roles)
        EnumSet.of(AccessType.READ, AccessType.CREATE).toList() -> SuperUser(userTypeId, userName, roles)
        EnumSet.of(AccessType.UPDATE, AccessType.DELETE).toList() -> Moderator(userTypeId, userName, roles)
        else -> throw IllegalArgumentException("unknown set")
    }
