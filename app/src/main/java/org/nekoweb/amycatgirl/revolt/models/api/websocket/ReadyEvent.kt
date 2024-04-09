package org.nekoweb.amycatgirl.revolt.models.api.websocket

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.nekoweb.amycatgirl.revolt.models.api.User

@Serializable
@SerialName("Ready")
data class ReadyEvent(val users: List<User>) : BaseEvent()
