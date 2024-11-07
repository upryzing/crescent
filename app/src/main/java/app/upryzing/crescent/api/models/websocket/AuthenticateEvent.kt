package app.upryzing.crescent.api.models.websocket

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Authenticate")
data class AuthenticateEvent(
    val token: String
): app.upryzing.crescent.api.models.websocket.BaseEvent()