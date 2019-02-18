package justbucket.ruherobase.data.mapper

import justbucket.ruherobase.data.model.AccessTypeEntity
import justbucket.ruherobase.data.model.UserEntity
import justbucket.ruherobase.data.model.UserTypeEntity
import justbucket.ruherobase.domain.model.*
import java.util.*

fun User.mapToData() = Triple(UserTypeEntity(id, name), accessTypeSet.mapToData(),

fun UserEntity.mapToDomain(accessEntities: List<AccessTypeEntity>) = when (accessEntities.mapToDomain()) {
    EnumSet.allOf(AccessType::class.java) -> Admin(userTypeId, userTypeName)
    EnumSet.of(AccessType.READ) -> SimpleUser(userTypeId, userTypeName)
    EnumSet.of(AccessType.READ, AccessType.COPY) -> SuperUser(userTypeId, userTypeName)
    EnumSet.of(AccessType.UPDATE, AccessType.DELETE) -> Moderator(userTypeId, userTypeName)
    else -> throw IllegalArgumentException("unknown set")
}
