package app.upryzing.crescent.api.featuregates

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class UpryzingExtension(val extensionName: String)
