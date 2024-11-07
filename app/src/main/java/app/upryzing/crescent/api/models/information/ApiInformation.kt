package app.upryzing.crescent.api.models.information

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ApiInformation(
    @SerialName("revolt") val version: String,
    val features: Services? = null,
    @SerialName("ws") val websocket: String,
    @SerialName("app") val officialClientLink: String,
    @SerialName("vapid") val webPushKey: String,
    @SerialName("build") val buildInformation: Build? = null
)