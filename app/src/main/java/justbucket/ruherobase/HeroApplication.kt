package justbucket.ruherobase

import android.app.Activity
import android.app.Application
import androidx.fragment.app.Fragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector
import justbucket.ruherobase.di.DaggerAppComponent
import javax.inject.Inject

/**
 * @author Roman Pliskin
 * @since 25.02.2019
 */
class HeroApplication : Application(), HasActivityInjector, HasSupportFragmentInjector {

    @Inject
    lateinit var androidActivityInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var androidFragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun activityInjector(): AndroidInjector<Activity> {
        return androidActivityInjector
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return androidFragmentInjector
    }

    override fun onCreate() {
        super.onCreate()

        DaggerAppComponent
                .builder()
                .application(this)
                .build()
                .inject(this)
    }

}