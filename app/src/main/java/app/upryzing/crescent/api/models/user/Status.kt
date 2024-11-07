package app.upryzing.crescent.api.models.user

import kotlinx.serialization.Serializable

@Serializable
data class Status(
    val text: String? = null,
    val presence: Presence? = null
)