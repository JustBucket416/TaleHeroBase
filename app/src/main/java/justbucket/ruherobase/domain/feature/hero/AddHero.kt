package justbucket.ruherobase.domain.feature.hero

import justbucket.ruherobase.domain.model.Hero
import justbucket.ruherobase.domain.repository.HeroRepository
import justbucket.ruherobase.domain.usecase.UseCase
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * @author Roman Pliskin
 * @since 18.02.2019
 */
class AddHero @Inject constructor (context: CoroutineContext,
                                   private val heroRepository: HeroRepository) :
    UseCase<Unit, AddHero.Params>(context) {

    override suspend fun run(params: Params?) {
        if (params == null) throw IllegalArgumentException(ILLEGAL_EXCEPTION_MESSAGE)
        heroRepository.addHero(params.hero, params.userId)
    }

    data class Params internal constructor(val hero: Hero, val userId: Long) {
        companion object {
            fun createParams(hero: Hero, userId: Long) = Params(hero, userId)
        }
    }
}