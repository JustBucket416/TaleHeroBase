package justbucket.ruherobase.domain.feature.role

import justbucket.ruherobase.domain.model.Role
import justbucket.ruherobase.domain.repository.RoleRepository
import justbucket.ruherobase.domain.usecase.UseCase
import kotlin.coroutines.CoroutineContext

/**
 * @author Roman Pliskin
 * @since 18.02.2019
 */
class GetAllRoles(context: CoroutineContext,
                  private val userRepository: RoleRepository)
    : UseCase<List<Role>, Nothing?>(context) {

    override suspend fun run(params: Nothing?): List<Role> {
        return userRepository.getAllRoles()
    }
}