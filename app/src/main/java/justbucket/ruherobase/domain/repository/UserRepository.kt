package justbucket.ruherobase.domain.repository

import justbucket.ruherobase.domain.model.User

interface UserRepository {

    suspend fun getAllUsers(): List<User>

    suspend fun addUser(user: User)

    suspend fun deleteUser(user: User)

    suspend fun updateUser(user: User)
}