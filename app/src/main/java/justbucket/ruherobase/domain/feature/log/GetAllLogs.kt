package justbucket.ruherobase.domain.feature.log

import justbucket.ruherobase.domain.model.LogEntry
import justbucket.ruherobase.domain.repository.LogEntryRepository
import justbucket.ruherobase.domain.usecase.UseCase
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * @author Roman Pliskin
 * @since 18.02.2019
 */
class GetAllLogs @Inject constructor(
    context: CoroutineContext,
    private val userRepository: LogEntryRepository
) : UseCase<List<LogEntry>, Nothing?>(context) {

    override suspend fun run(params: Nothing?): List<LogEntry> {
        return userRepository.getLogs()
    }
}