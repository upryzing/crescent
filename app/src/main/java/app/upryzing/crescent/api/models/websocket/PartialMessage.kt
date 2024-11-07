package app.upryzing.crescent.api.models.websocket

import app.upryzing.crescent.api.models.attachments.Attachment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Message")
data class PartialMessage(
    @SerialName("_id")
    val id: String? = null,
    @SerialName("channel")
    val channelId: String? = null,
    @SerialName("author")
    val authorId: String? = null,
    val content: String? = null,
    val attachments: List<Attachment>? = null,
    val edited: String? = null,
    val replies: List<String>? = null,
    val system: app.upryzing.crescent.api.models.websocket.SystemMessage? = null,
) : app.upryzing.crescent.api.models.websocket.BaseEvent()
