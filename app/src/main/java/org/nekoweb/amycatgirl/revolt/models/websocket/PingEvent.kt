package org.nekoweb.amycatgirl.revolt.models.websocket

import kotlinx.serialization.Serializable

@Serializable
data class PingEvent(val data: Int) : BaseEvent("Ping")

@Serializable
data class PongEvent(val data: Int) : BaseEvent("Pong")