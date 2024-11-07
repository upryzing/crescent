package app.upryzing.crescent.api

data class ApiOptions(
    val apiUrl: String = "https://api.revolt.chat/",
    val reconnectTimeout: Int = 10000
)