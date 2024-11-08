package app.upryzing.crescent.api.models.user

import app.upryzing.crescent.api.RevoltAPI
import app.upryzing.crescent.api.models.attachments.Attachment
import app.upryzing.crescent.api.models.friends.Relation
import app.upryzing.crescent.api.models.friends.RelationshipStatus
import io.ktor.client.call.body
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class User(
    @SerialName("_id") val id: String,
    val username: String,
    @SerialName("display_name") val displayName: String? = null,
    val discriminator: String,
    val avatar: Attachment? = null,
    val badges: Int? = null,
    val flags: Int? = null,
    val status: Status? = null,
    val relations: List<Relation>? = null,
    val relationship: RelationshipStatus? = null,
    val online: Boolean? = null,
    @Transient val client: RevoltAPI? = null
) {
    val profile
         get() = run {
             var result: Profile? = null
             CoroutineScope(Dispatchers.IO).launch {
                 result = client!!.http.get("users/$id/profile").body<Profile>()
             }

             result
         }

    val profilePicture
        get() = if (avatar != null)
            "${client?.connection?.cdn}/avatars/${avatar.id}"
        else
            "${client?.connection?.api}/users/$id/default_avatar"
}