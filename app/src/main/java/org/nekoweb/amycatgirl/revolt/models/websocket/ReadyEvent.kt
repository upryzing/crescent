package org.nekoweb.amycatgirl.revolt.models.websocket

import kotlinx.serialization.Serializable
import org.nekoweb.amycatgirl.revolt.models.api.User

@Serializable
data class ReadyEvent(val users: List<User>) : BaseEvent("Ready")
