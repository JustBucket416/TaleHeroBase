package justbucket.ruherobase.domain.repository

import justbucket.ruherobase.domain.model.Role

interface RoleRepository {

    suspend fun getAllRoles() : List<Role>

    suspend fun addRole(role: Role)

    suspend fun updateRole(role: Role)

    suspend fun deleteRole(role: Role)
}