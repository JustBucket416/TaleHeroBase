package justbucket.ruherobase.domain.feature.role

import justbucket.ruherobase.domain.model.Role
import justbucket.ruherobase.domain.repository.RoleRepository
import justbucket.ruherobase.domain.usecase.UseCase
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * @author Roman Pliskin
 * @since 18.02.2019
 */
class DeleteRole @Inject constructor(
    context: CoroutineContext,
    private val userRepository: RoleRepository
) : UseCase<Unit, Role>(context) {

    override suspend fun run(params: Role?) {
        if (params == null) throw IllegalArgumentException(ILLEGAL_EXCEPTION_MESSAGE)
        userRepository.deleteRole(params)
    }
}