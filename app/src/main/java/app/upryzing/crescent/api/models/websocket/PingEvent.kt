package app.upryzing.crescent.api.models.websocket

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Ping")
data class PingEvent(val data: Int) : app.upryzing.crescent.api.models.websocket.BaseEvent()

@Serializable
@SerialName("Pong")
data class PongEvent(val data: Int) : app.upryzing.crescent.api.models.websocket.BaseEvent()