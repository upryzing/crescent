package app.upryzing.crescent.api.models.websocket

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Authenticated")
data object AuthenticatedEvent : app.upryzing.crescent.api.models.websocket.BaseEvent()