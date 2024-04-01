package org.nekoweb.amycatgirl.revolt.models.api

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val username: String,
    val displayName: String,
    val badges: Int,
    val flags: Int,
    val relations: List<Relation>,
    val relationship: RelationshipStatus,
    val online: Boolean?
)

@Serializable
enum class AccountFlags(flag: Int) {
    NONE(0),
    SUSPENDED(1),
    DELETED(2),
    BANNED(4),
    SPAM(8)
}