package app.upryzing.crescent.di.components

import app.upryzing.crescent.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component
interface ApplicationComponent {
    fun inject(activity: MainActivity)
}