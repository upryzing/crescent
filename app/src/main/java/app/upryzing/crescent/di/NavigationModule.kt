package app.upryzing.crescent.di

import app.upryzing.crescent.api.RevoltAPI
import app.upryzing.crescent.ui.navigation.Navigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NavigationModule {
    @Provides
    @Singleton
    fun provideNavigation(): Navigator {
        return Navigator()
    }
}