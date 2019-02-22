package justbucket.ruherobase.domain.feature.user

import justbucket.ruherobase.domain.model.User
import justbucket.ruherobase.domain.repository.UserRepository
import justbucket.ruherobase.domain.usecase.UseCase
import kotlin.coroutines.CoroutineContext

/**
 * @author Roman Pliskin
 * @since 18.02.2019
 */
class GetAllUsers(context: CoroutineContext,
                  private val userRepository: UserRepository)
    : UseCase<List<User>, Nothing?>(context) {

    override suspend fun run(params: Nothing?): List<User> {
        return userRepository.getAllUsers()
    }
}