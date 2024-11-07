package app.upryzing.crescent.api.models.user

import kotlinx.serialization.Serializable

@Serializable
enum class Flags(flag: Int) {
    NONE(0),
    SUSPENDED(1),
    DELETED(2),
    BANNED(4),
    SPAM(8)
}