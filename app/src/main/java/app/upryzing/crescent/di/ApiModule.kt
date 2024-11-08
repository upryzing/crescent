package app.upryzing.crescent.di

import app.upryzing.crescent.api.RevoltAPI
import dagger.Module
import dagger.Provides
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