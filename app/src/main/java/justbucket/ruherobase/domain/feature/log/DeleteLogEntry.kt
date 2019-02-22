package justbucket.ruherobase.domain.feature.log

import justbucket.ruherobase.domain.repository.LogEntryRepository
import justbucket.ruherobase.domain.usecase.UseCase
import kotlin.coroutines.CoroutineContext

/**
 * @author Roman Pliskin
 * @since 18.02.2019
 */
class DeleteLogs(context: CoroutineContext,
                     private val userRepository: LogEntryRepository)
    : UseCase<Unit, Nothing?>(context) {

    override suspend fun run(params: Nothing?) {
        userRepository.clearLogs()
    }
}