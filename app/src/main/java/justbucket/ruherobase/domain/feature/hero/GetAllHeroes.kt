package justbucket.ruherobase.domain.feature.hero

import justbucket.ruherobase.domain.model.Hero
import justbucket.ruherobase.domain.repository.HeroRepository
import justbucket.ruherobase.domain.usecase.UseCase
import kotlin.coroutines.CoroutineContext

/**
 * @author Roman Pliskin
 * @since 18.02.2019
 */
class GetAllHeroes(context: CoroutineContext,
                   private val heroRepository: HeroRepository)
    : UseCase<List<Hero>, Nothing?>(context) {

    override suspend fun run(params: Nothing?): List<Hero> {
        return heroRepository.getAllHeroes()
    }
}