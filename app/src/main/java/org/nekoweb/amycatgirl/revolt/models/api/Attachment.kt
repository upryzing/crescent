package org.nekoweb.amycatgirl.revolt.models.api

import kotlinx.serialization.Serializable
import org.nekoweb.amycatgirl.revolt.models.api.attachments.FileMetadata

@Serializable
data class Attachment(
    val id: String,
    val tag: String,
    val filename: String,
    val mimeType: String,
    val size: Int,
    val metadata: FileMetadata,
    val deleted: Boolean? = null,
    val reported: Boolean? = null,
    val messageId: String? = null,
    val userId: String? = null,
    val serverId: String? = null
)