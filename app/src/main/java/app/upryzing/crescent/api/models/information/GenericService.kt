package app.upryzing.crescent.api.models.information

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenericService(
    @SerialName("enabled") val isAvailable: Boolean,
    val url: String
)