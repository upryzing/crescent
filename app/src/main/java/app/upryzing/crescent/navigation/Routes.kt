package app.upryzing.crescent.navigation

object Routes {
    const val DEBUG = "debug"
    const val AUTH = "auth"

    object AuthSubRoutes {
        const val MFA = "auth/mfa"
    }

    const val HOME = "home"
    const val START_CONVERSATION = "home/start_conversation"

    object Messages {
        const val ROUTE_PATTERN = "messages/{id}"
        fun destination(id: String) = "messages/$id"
    }

    const val SETTINGS = "settings"
    object SettingsSubRoutes {
        const val ACCOUNT = "settings/account"
        const val PROFILE = "settings/profile"
        const val CLIENT = "settings/client"
        const val ABOUT = "settings/about"
    }
}