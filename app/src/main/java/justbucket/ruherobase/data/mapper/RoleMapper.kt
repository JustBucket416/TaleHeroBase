package justbucket.ruherobase.data.mapper

import justbucket.ruherobase.data.model.RoleEntity
import justbucket.ruherobase.domain.model.AccessType
import justbucket.ruherobase.domain.model.Role
import java.util.*

fun Role.mapToData() = Pair(RoleEntity(id, roleName), accessTypes.mapToData())

fun RoleEntity.mapToDomain(entities: List<AccessType>) = Role(roleId, EnumSet.copyOf(entities), roleName)