package justbucket.ruherobase.domain.feature.hero

import justbucket.ruherobase.domain.model.Hero
import justbucket.ruherobase.domain.repository.HeroRepository
import justbucket.ruherobase.domain.usecase.UseCase
import java.lang.IllegalArgumentException
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * @author Roman Pliskin
 * @since 18.02.2019
 */
class DeleteHero @Inject constructor (context: CoroutineContext,
                                      private val heroRepository: HeroRepository)
    : UseCase<Unit, Hero> (context) {

    override suspend fun run(params: Hero?) {
        if (params == null) throw IllegalArgumentException(ILLEGAL_EXCEPTION_MESSAGE)
        heroRepository.deleteHero(params)
    }
}