package justbucket.ruherobase.di

import android.content.Context
import dagger.Module
import dagger.Provides
import justbucket.ruherobase.data.HeroRepositoryImpl
import justbucket.ruherobase.data.LogEntryRepositoryImpl
import justbucket.ruherobase.data.RoleRepositoryImpl
import justbucket.ruherobase.data.UserRepositoryImpl
import justbucket.ruherobase.data.database.HeroDatabase
import justbucket.ruherobase.domain.repository.HeroRepository
import justbucket.ruherobase.domain.repository.LogEntryRepository
import justbucket.ruherobase.domain.repository.RoleRepository
import justbucket.ruherobase.domain.repository.UserRepository
import javax.inject.Singleton

/**
 * @author Roman Pliskin
 * @since 25.02.2019
 */
@Module
class DataModule {

    @Provides
    @Singleton
    fun provideHeroDatabase(context: Context): HeroDatabase {
        return HeroDatabase.getDatabase(context)
    }

    @Provides
    fun provideHeroRepository(database: HeroDatabase): HeroRepository {
        return HeroRepositoryImpl(database)
    }

    @Provides
    fun provideLogEntryRepository(database: HeroDatabase): LogEntryRepository {
        return LogEntryRepositoryImpl(database)
    }

    @Provides
    fun provideRoleRepository(database: HeroDatabase): RoleRepository {
        return RoleRepositoryImpl(database)
    }

    @Provides
    fun provideUserRepository(database: HeroDatabase): UserRepository {
        return UserRepositoryImpl(database)
    }
}