package org.nekoweb.amycatgirl.revolt.models.api.websocket

import kotlinx.serialization.Serializable

@Serializable
data class AuthenticateEvent(
    val type: String,
    val token: String
)