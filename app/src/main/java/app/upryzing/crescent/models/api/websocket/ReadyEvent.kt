package app.upryzing.crescent.models.api.websocket

import app.upryzing.crescent.models.api.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Ready")
data class ReadyEvent(val users: List<User>) : BaseEvent()
