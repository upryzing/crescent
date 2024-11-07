package app.upryzing.crescent.api.models.information

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LegacyVoiceService(
    val url: String,
    @SerialName("enabled") val isAvailable: Boolean,
    @SerialName("ws") val socket: String
)
