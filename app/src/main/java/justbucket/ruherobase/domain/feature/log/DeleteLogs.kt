package justbucket.ruherobase.domain.feature.log

import justbucket.ruherobase.domain.repository.LogEntryRepository
import justbucket.ruherobase.domain.usecase.UseCase
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * @author Roman Pliskin
 * @since 18.02.2019
 */
class DeleteLogs @Inject constructor(
    context: CoroutineContext,
    private val userRepository: LogEntryRepository
) : UseCase<Unit, Nothing?>(context) {

    override suspend fun run(params: Nothing?) {
        userRepository.clearLogs()
    }
}