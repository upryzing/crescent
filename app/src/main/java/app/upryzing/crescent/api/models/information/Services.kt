package app.upryzing.crescent.api.models.information

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Services(
    @SerialName("email") val requiresEmailVerification: Boolean,
    @SerialName("invite_only") val requiresInviteCode: Boolean,
    val captcha: HCaptcha? = null,
    @SerialName("autumn") val cdn: GenericService? = null,
    @SerialName("january") val proxy: GenericService? = null,
    @SerialName("voso") val legacyVoice: LegacyVoiceService? = null,
)