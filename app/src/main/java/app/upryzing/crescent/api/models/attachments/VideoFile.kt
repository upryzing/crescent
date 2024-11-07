package app.upryzing.crescent.api.models.attachments

import kotlinx.serialization.Serializable

@Serializable
data class VideoFile(
    val width: Int,
    val height: Int
) : app.upryzing.crescent.api.models.attachments.FileMetadata("Video")
