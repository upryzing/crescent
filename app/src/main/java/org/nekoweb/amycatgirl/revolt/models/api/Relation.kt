package org.nekoweb.amycatgirl.revolt.models.api

import kotlinx.serialization.Serializable

@Serializable
data class Relation(
    val userId: String,
    val relationshipStatus: RelationshipStatus
)

@Serializable
enum class RelationshipStatus(status: String) {
    NONE("None"),
    USER("User"),
    FRIEND("Friend"),
    OUTGOING_REQUEST("Outgoing"),
    INCOMING_REQUEST("Incoming"),
    BLOCKED_BY_USER("Blocked"),
    BLOCKED_BY_OTHER("BlockedOther")
}