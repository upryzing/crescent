package app.upryzing.crescent.api.featureFlags

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class UpryzingExtension(val extensionName: String)
