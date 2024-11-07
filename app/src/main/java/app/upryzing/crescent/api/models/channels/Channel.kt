package app.upryzing.crescent.api.models.channels

import app.upryzing.crescent.api.RevoltAPI
import app.upryzing.crescent.api.models.attachments.Attachment
import app.upryzing.crescent.api.models.websocket.PartialMessage
import io.ktor.client.call.body
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("channel_type")
sealed class Channel(
    @Transient open val client: RevoltAPI? = null
) {
    @SerialName("_id")
    abstract val id: String

    @Serializable
    @SerialName("SavedMessages")
    class SavedMessages(
        @SerialName("_id")
        override val id: String,
        @SerialName("user")
        val ownerId: String,
        @Transient override val client: RevoltAPI? = null,
    ) : Channel(client)

    @Serializable
    @SerialName("DirectMessage")
    class DirectMessage(
        @SerialName("_id")
        override val id: String,
        val active: Boolean,
        val recipients: List<String>,
        @SerialName("last_message_id")
        val lastSentMessage: String? = null,
        @Transient override val client: RevoltAPI? = null,
    ) : Channel(client)

    @Serializable
    @SerialName("Group")
    class Group(
        @SerialName("_id")
        override val id: String,
        val name: String,
        @SerialName("owner")
        val ownerId: String,
        val description: String? = null,
        val recipients: List<String>,
        val icon: Attachment? = null,
        @SerialName("last_message_id")
        val lastSentMessage: String? = null,
        @SerialName("permissions")
        val memberPermissions: Int? = null,
        @SerialName("nsfw")
        val notSafeForWork: Boolean? = null,
        @Transient override val client: RevoltAPI? = null,
    ) : Channel(client)

    suspend fun getMessages(): List<PartialMessage>? {
        require(client != null)

        return client?.http?.get("channels/$id/messages")?.body()
    }

    suspend fun getMessage(messageId: String): PartialMessage? {
        require(client != null)

        return client?.http?.get("channels/${this.id}/messages/$messageId")?.body()
    }

    suspend fun sendMessage(content: String) {
        require(client != null)
        client?.http?.post("channels/$id/messages") {
            setBody(PartialMessage(
                content = content
            ))
        }
    }

    suspend fun sendMessage(content: PartialMessage) {
        require(client != null)
        client?.http?.post("channels/$id/messages") {
            setBody(content)
        }
    }
}