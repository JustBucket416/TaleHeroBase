package justbucket.ruherobase.domain.feature.log

import justbucket.ruherobase.domain.model.LogEntry
import justbucket.ruherobase.domain.repository.LogEntryRepository
import justbucket.ruherobase.domain.usecase.UseCase
import kotlin.coroutines.CoroutineContext

/**
 * @author Roman Pliskin
 * @since 18.02.2019
 */
class GetAllLogs(context: CoroutineContext,
                 private val userRepository: LogEntryRepository)
    : UseCase<List<LogEntry>, Nothing?>(context) {

    override suspend fun run(params: Nothing?): List<LogEntry> {
        return userRepository.getLogs()
    }
}