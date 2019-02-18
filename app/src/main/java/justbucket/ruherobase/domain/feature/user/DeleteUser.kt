package justbucket.ruherobase.domain.feature.user

import justbucket.ruherobase.domain.model.User
import justbucket.ruherobase.domain.repository.UserRepository
import justbucket.ruherobase.domain.usecase.UseCase
import kotlin.coroutines.CoroutineContext

/**
 * @author Roman Pliskin
 * @since 18.02.2019
 */
class DeleteUser(context: CoroutineContext,
                 private val userRepository: UserRepository)
    : UseCase<Unit, User>( context) {

    override suspend fun run(params: User?) {
        if (params == null) throw IllegalArgumentException(ILLEGAL_EXCEPTION_MESSAGE)
        userRepository.deleteUser(params)
    }
}