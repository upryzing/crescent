package app.upryzing.crescent.api.models.websocket

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("type")
sealed class SystemMessage {
    @SerialName("text")
    data class Text(val content: String) : app.upryzing.crescent.api.models.websocket.SystemMessage()

    @Serializable
    @SerialName("user_added")
    data class UserAdded(val id: String, val by: String) : app.upryzing.crescent.api.models.websocket.SystemMessage()

    @Serializable
    @SerialName("user_remove")
    data class UserRemove(val id: String, val by: String) : app.upryzing.crescent.api.models.websocket.SystemMessage()

    @Serializable
    @SerialName("channel_renamed")
    data class UserKicked(val name: String, val by: String) : app.upryzing.crescent.api.models.websocket.SystemMessage()

    @Serializable
    @SerialName("channel_description_changed")
    data class ChannelDescriptionChanged(val by: String) : app.upryzing.crescent.api.models.websocket.SystemMessage()

    @Serializable
    @SerialName("channel_icon_changed")
    data class ChannelIconChanged(val by: String) : app.upryzing.crescent.api.models.websocket.SystemMessage()

    @Serializable
    @SerialName("channel_ownership_changed")
    data class ChannelTransferred(val from: String, val to: String) : app.upryzing.crescent.api.models.websocket.SystemMessage()

    @Serializable
    data object UnimplementedMessage : app.upryzing.crescent.api.models.websocket.SystemMessage()
}