package app.upryzing.crescent.di.modules

import app.upryzing.crescent.CrescentApplication
import app.upryzing.crescent.api.ApiOptions
import app.upryzing.crescent.api.RevoltAPI
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.DefineComponent
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    @Singleton
    fun provideClient(): RevoltAPI {
        return RevoltAPI()
    }
}