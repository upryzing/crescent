package app.upryzing.crescent.models.api.attachments

import kotlinx.serialization.Serializable

@Serializable
data class ImageFile(
    val width: Int,
    val height: Int
) : FileMetadata("Image")
