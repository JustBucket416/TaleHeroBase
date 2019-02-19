package justbucket.ruherobase.domain.feature.log

import justbucket.ruherobase.domain.model.LogEntry
import justbucket.ruherobase.domain.repository.LogEntryRepository
import justbucket.ruherobase.domain.usecase.UseCase
import kotlin.coroutines.CoroutineContext

/**
 * @author Roman Pliskin
 * @since 18.02.2019
 */
class AddLogEntry(context: CoroutineContext,
             private val userEntryRepository: LogEntryRepository)
    : UseCase<Unit, LogEntry>(context) {

    override suspend fun run(params: LogEntry?) {
        if (params == null) throw IllegalArgumentException(ILLEGAL_EXCEPTION_MESSAGE)
        userEntryRepository.insertLog(params)
    }
}