package org.nekoweb.amycatgirl.revolt.models.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Message")
data class PartialMessage(
    @SerialName("_id")
    val id: String,
    val nonce: String,
    @SerialName("channel")
    val channelId: String,
    @SerialName("author")
    val authorId: String,
    val content: String? = null,
    val attachments: List<Attachment>? = null,
    val edited: String? = null,
    val replies: List<String>? = null,
)
