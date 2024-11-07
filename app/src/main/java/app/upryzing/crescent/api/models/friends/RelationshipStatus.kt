package app.upryzing.crescent.api.models.friends

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class RelationshipStatus {
    @SerialName("None") NONE,
    @SerialName("User") USER,
    @SerialName("Friend") FRIEND,
    @SerialName("Outgoing") OUTGOING_REQUEST,
    @SerialName("Incoming") INCOMING_REQUEST,
    @SerialName("Blocked") BLOCKED_BY_USER,
    @SerialName("BlockedOther") BLOCKED_BY_OTHER
}