package justbucket.ruherobase.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import justbucket.ruherobase.presentation.*

/**
 * @author Roman Pliskin
 * @since 25.02.2019
 */
@Module
abstract class UIModule {

    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun bindDetailActivit(): DetailActivity

    @ContributesAndroidInjector
    abstract fun bindLogActivity(): LogActivity

    @ContributesAndroidInjector
    abstract fun bindChooseUserActivity(): ChooseUserActivity

    @ContributesAndroidInjector
    abstract fun bindAddUseeFragment(): AddUserFragment
}