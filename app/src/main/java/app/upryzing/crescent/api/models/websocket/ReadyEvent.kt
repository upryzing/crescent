package app.upryzing.crescent.api.models.websocket

import app.upryzing.crescent.api.models.user.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Ready")
data class ReadyEvent(val users: List<User>) : app.upryzing.crescent.api.models.websocket.BaseEvent()
