package org.nekoweb.amycatgirl.revolt.models.websocket

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Authenticated")
data object AuthenticatedEvent : BaseEvent()