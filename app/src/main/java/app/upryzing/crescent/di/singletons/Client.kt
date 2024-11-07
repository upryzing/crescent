package app.upryzing.crescent.di.singletons

import app.upryzing.crescent.api.ApiOptions
import app.upryzing.crescent.api.RevoltAPI
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Client @Inject constructor(options: ApiOptions?) : RevoltAPI(options)