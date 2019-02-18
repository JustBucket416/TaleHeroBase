package justbucket.ruherobase.data

import justbucket.ruherobase.data.database.HeroDatabase
import justbucket.ruherobase.data.mapper.mapToData
import justbucket.ruherobase.data.mapper.mapToDomain
import justbucket.ruherobase.domain.model.Role
import justbucket.ruherobase.domain.repository.RoleRepository

class RoleRepositoryImpl(database: HeroDatabase) : RoleRepository {

    private val roleDao = database.getRoleDao()

    override suspend fun getAllRoles(): List<Role> {
        return roleDao.getAllRoles().map { it.mapToDomain() }
    }

    override suspend fun addRole(role: Role) {
        roleDao.insertRole(role.mapToData())
    }

    override suspend fun updateRole(role: Role) {
        roleDao.updateRole(role.mapToData())
    }

    override suspend fun deleteRole(role: Role) {
        roleDao.deleteRole(role.mapToData())
    }
}