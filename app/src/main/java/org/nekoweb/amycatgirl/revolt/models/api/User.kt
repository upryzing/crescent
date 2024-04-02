package org.nekoweb.amycatgirl.revolt.models.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("_id")
    val id: String,
    val username: String,
    val discriminator: String,
    @SerialName("display_name")
    val displayName: String? = null,
    val badges: Int? = null,
    val avatar: Attachment? = null,
    val flags: Int? = null,
    val relations: List<Relation>? = null,
    val relationship: RelationshipStatus? = null,
    val online: Boolean? = null
)

@Serializable
enum class Flags(flag: Int) {
    NONE(0),
    SUSPENDED(1),
    DELETED(2),
    BANNED(4),
    SPAM(8)
}