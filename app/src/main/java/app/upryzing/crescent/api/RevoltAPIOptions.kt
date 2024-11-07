package app.upryzing.crescent.api

data class RevoltAPIOptions(
    val apiUrl: String = "https://api.revolt.chat",
    val reconnectTimeout: Int = 10000
)