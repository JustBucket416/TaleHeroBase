package justbucket.ruherobase.data.mapper

import justbucket.ruherobase.data.model.RoleEntity
import justbucket.ruherobase.domain.model.Role

fun Role.mapToData() = RoleEntity(id, accessTypes.mapToData())

fun RoleEntity.mapToDomain() = Role(roleId, userEntity.mapToDomain(), accessTypeEntities.mapToDomain())