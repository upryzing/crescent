package app.upryzing.crescent.api.models.user

import app.upryzing.crescent.api.models.attachments.Attachment
import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val content: String? = null,
    val background: Attachment? = null
)