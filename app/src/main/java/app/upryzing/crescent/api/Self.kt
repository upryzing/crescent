package app.upryzing.crescent.api

import app.upryzing.crescent.api.models.attachments.Attachment
import app.upryzing.crescent.api.models.channels.Channel
import app.upryzing.crescent.api.models.friends.Relation
import app.upryzing.crescent.api.models.friends.RelationshipStatus
import app.upryzing.crescent.api.models.user.Flags
import app.upryzing.crescent.api.models.user.Status
import app.upryzing.crescent.api.models.user.User
import io.ktor.client.call.body

class Self(
    id: String,
    relations: List<Relation>?,
    flags: Int?,
    avatar: Attachment?,
    badges: Int,
    online: Boolean,
    status: Status,
    username: String,
    override val relationship: RelationshipStatus = RelationshipStatus.USER,
    discriminator: String,
    displayName: String,
    override var client: RevoltAPI
) : User(
    id = id,
    relations = relations,
    flags = flags,
    avatar = avatar,
    badges = badges,
    client = client,
    online = online,
    status = status,
    username = username,
    relationship = relationship,
    discriminator = discriminator,
    displayName = displayName
) {
    suspend fun getDirectMessages(): List<Channel> {
        return client!!.http.get("users/dms").body<List<Channel>>()
    }
}