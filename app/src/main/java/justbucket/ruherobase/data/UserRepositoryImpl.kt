package justbucket.ruherobase.data

import justbucket.ruherobase.data.database.HeroDatabase
import justbucket.ruherobase.data.mapper.mapAccessLongToDomain
import justbucket.ruherobase.data.mapper.mapToData
import justbucket.ruherobase.data.mapper.mapToDomain
import justbucket.ruherobase.data.model.UserRoleLinkEntity
import justbucket.ruherobase.domain.model.User
import justbucket.ruherobase.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(database: HeroDatabase) : UserRepository {

    private val userDao = database.getUserDao()
    private val userRoleLinkDao = database.getUserRoleLinkDao()
    private val userAccessTypeDao = database.getUserAccessLinkDao()
    private val roleDao = database.getRoleDao()
    private val roleLinkDao = database.getRoleAccessLinkDao()

    override suspend fun getAllUsers(): List<User> {
        return userDao.getAllUsers().map { userEntity ->
            userEntity.mapToDomain(userAccessTypeDao.getAllUserLinks(userEntity.userId!!).map { mapAccessLongToDomain(it) },
                    userRoleLinkDao.getAllRoleLinks(userEntity.userId).map { roleId ->
                        roleDao.getRoleById(roleId)
                                .mapToDomain(roleLinkDao.getAllRoleLinks(roleId).map { mapAccessLongToDomain(it) })
                    })
        }
    }

    override suspend fun addUser(user: User) {
        val (entity, accessTypes, roles) = user.mapToData()
        val id = userDao.insertUser(entity)
        /*accessTypes.forEach {
            userAccessTypeDao.insertUserRoleLink(UserAccessLinkEntity(id, it.accessId))
        }*/
        roles?.forEach {
            userRoleLinkDao.insertUserRoleLink(UserRoleLinkEntity(id, it.first.roleId!!))
        }
    }

    override suspend fun deleteUser(user: User) {
        userDao.deleteUser(user.mapToData().first)
    }

    override suspend fun updateUser(user: User) {
        val (entity, accessTypes, roles) = user.mapToData()
        userDao.updateUser(entity)
        //userAccessTypeDao.deleteAllUserLinks(entity.userId!!)
        /*accessTypes.forEach {
            userAccessTypeDao.insertUserRoleLink(UserAccessLinkEntity(entity.userId, it.accessId))
        }*/
        userRoleLinkDao.deleteAllRoleLinks(entity.userId!!)
        roles?.forEach {
            userRoleLinkDao.insertUserRoleLink(UserRoleLinkEntity(entity.userId, it.first.roleId!!))
        }
    }
}