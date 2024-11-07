package app.upryzing.crescent.api.models.information

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HCaptcha(
    @SerialName("enabled") val isAvailable: Boolean,
    val key: String
)
