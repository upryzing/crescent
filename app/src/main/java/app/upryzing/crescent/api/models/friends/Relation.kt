package app.upryzing.crescent.api.models.friends

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Relation(
    @SerialName("_id") val userId: String,
    @SerialName("status") val relationshipStatus: RelationshipStatus
)