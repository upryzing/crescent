package app.upryzing.crescent.api.models.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Presence(value: String) {
    @SerialName("Online") ONLINE("Online"),
    @SerialName("Focus") FOCUS("Focus"),
    @SerialName("Busy") DO_NOT_DISTURB("Busy"),
    @SerialName("Idle") IDLE("Idle"),
    @SerialName("Invisible") OFFLINE("Invisible")
}