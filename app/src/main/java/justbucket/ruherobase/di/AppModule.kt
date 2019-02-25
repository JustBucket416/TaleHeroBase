package justbucket.ruherobase.di

import android.app.Application
import android.content.Context
import com.bumptech.glide.Glide
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers

/**
 * @author Roman Pliskin
 * @since 25.02.2019
 */
@Module
abstract class AppModule {

    @Module
    companion object {

        @Provides
        @JvmStatic
        fun provideGlideManager(context: Context) = Glide.with(context)

        @Provides
        @JvmStatic
        fun provideCoroutineContext(): kotlin.coroutines.CoroutineContext = Dispatchers.Main
    }

    @Binds
    abstract fun bindAppContext(application: Application): Context

}