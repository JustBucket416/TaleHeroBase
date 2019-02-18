package justbucket.ruherobase.domain.feature.hero

import justbucket.ruherobase.domain.model.Hero
import justbucket.ruherobase.domain.repository.HeroRepository
import justbucket.ruherobase.domain.usecase.UseCase
import kotlin.coroutines.CoroutineContext

/**
 * @author Roman Pliskin
 * @since 18.02.2019
 */
class AddHero(context: CoroutineContext,
              private val heroRepository: HeroRepository)
    : UseCase<Unit, Hero>(context) {

    override suspend fun run(params: Hero?) {
        if (params == null) throw IllegalArgumentException(ILLEGAL_EXCEPTION_MESSAGE)
        heroRepository.addHero(params)
    }
}