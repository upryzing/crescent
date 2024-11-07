package app.upryzing.crescent.api.models.attachments

import kotlinx.serialization.Serializable

@Serializable
data class ImageFile(
    val width: Int,
    val height: Int
) : app.upryzing.crescent.api.models.attachments.FileMetadata("Image")
