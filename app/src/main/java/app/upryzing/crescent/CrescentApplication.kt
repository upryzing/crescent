package app.upryzing.crescent

import android.app.Application
import app.upryzing.crescent.di.components.ApplicationComponent

class CrescentApplication : Application() {
    lateinit var crescentAppComponent: ApplicationComponent
        private set

    override fun onCreate() {
        super.onCreate()

        crescentAppComponent = DaggerApplicationComponent.create()
    }
}