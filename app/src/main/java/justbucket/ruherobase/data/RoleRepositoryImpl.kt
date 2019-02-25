package justbucket.ruherobase.data

import justbucket.ruherobase.data.database.HeroDatabase
import justbucket.ruherobase.data.mapper.mapAccessLongToDomain
import justbucket.ruherobase.data.mapper.mapToData
import justbucket.ruherobase.data.mapper.mapToDomain
import justbucket.ruherobase.data.model.RoleAccessLinkEntity
import justbucket.ruherobase.domain.model.Role
import justbucket.ruherobase.domain.repository.RoleRepository
import javax.inject.Inject

class RoleRepositoryImpl @Inject constructor(database: HeroDatabase) : RoleRepository {

    private val roleDao = database.getRoleDao()
    private val roleLinkDao = database.getRoleAccessLinkDao()

    override suspend fun getAllRoles(): List<Role> {
        return roleDao.getAllRoles()
                .map {
                    it.mapToDomain(roleLinkDao.getAllRoleLinks(it.roleId!!).map { mapAccessLongToDomain(it) })
                }
    }

    override suspend fun addRole(role: Role) {
        val (entity, links) = role.mapToData()
        val roleId = roleDao.insertRole(entity)
        links.forEach {
            roleLinkDao.insertRoleAccessLink(RoleAccessLinkEntity(it.accessId, roleId))
        }
    }

    override suspend fun updateRole(role: Role) {
        val (entity, links) = role.mapToData()
        roleDao.updateRole(entity)
        roleLinkDao.deleteAllRoleLinks(entity.roleId!!)
        links.forEach {
            roleLinkDao.insertRoleAccessLink(RoleAccessLinkEntity(entity.roleId, it.accessId))
        }
    }

    override suspend fun deleteRole(role: Role) {
        roleDao.deleteRole(role.mapToData().first)
    }
}