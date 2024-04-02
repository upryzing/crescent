package org.nekoweb.amycatgirl.revolt.models.websocket

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.nekoweb.amycatgirl.revolt.models.api.Attachment

@Serializable
@SerialName("Message")
data class PartialMessageEvent(
    @SerialName("_id")
    val id: String,
    @SerialName("channel")
    val channelId: String,
    @SerialName("author")
    val authorId: String,
    val content: String? = null,
    val attachments: List<Attachment>? = null,
    val edited: String? = null,
    @SerialName("replies")
    val replyIds: List<String>? = null
) : BaseEvent()