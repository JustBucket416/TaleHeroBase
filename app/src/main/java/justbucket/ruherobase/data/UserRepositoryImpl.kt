package justbucket.ruherobase.data

import justbucket.ruherobase.data.database.HeroDatabase
import justbucket.ruherobase.data.mapper.mapToData
import justbucket.ruherobase.domain.model.User
import justbucket.ruherobase.domain.repository.UserRepository

class UserRepositoryImpl(database: HeroDatabase) : UserRepository {

    private val userDao = database.getUserDao()

    override suspend fun getAllUsers(): List<User> {
        return userDao.getAllUsers().map { it.mapToDomain() }
    }

    override suspend fun addUser(user: User) {
        userDao.insertUser(user.mapToData())
    }

    override suspend fun deleteUser(user: User) {
        userDao.deleteUser(user.mapToData())
    }

    override suspend fun updateUser(user: User) {
        userDao.updateUser(user.mapToData())
    }
}