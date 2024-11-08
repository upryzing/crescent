package app.upryzing.crescent.api

import app.upryzing.crescent.api.models.attachments.Attachment
import app.upryzing.crescent.api.models.channels.Channel
import app.upryzing.crescent.api.models.friends.Relation
import app.upryzing.crescent.api.models.friends.RelationshipStatus
import app.upryzing.crescent.api.models.user.Status
import app.upryzing.crescent.api.models.user.User
import io.ktor.client.call.body
import kotlinx.serialization.Transient

class Self(
    val user: User,
    val client: RevoltAPI
) {
    suspend fun getDirectMessages(): List<Channel> {
        return client.http.get("users/dms").body<List<Channel>>()
    }
}