package app.upryzing.crescent.api

data class ConnectionDetails(
    val api: String,
    val websocket: String,
    val cdn: String? = null,
    val proxy: String? = null,
    val version: String
)