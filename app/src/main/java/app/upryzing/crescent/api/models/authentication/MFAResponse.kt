package app.upryzing.crescent.api.models.authentication

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class MFAResponse {
    @Serializable
    data class PasswordMFA(val password: String): app.upryzing.crescent.api.models.authentication.MFAResponse()
    @Serializable
    data class TwoFactorMFA(@SerialName("totp_code") val code: String): app.upryzing.crescent.api.models.authentication.MFAResponse()
    @Serializable
    data class RecoveryMFA(@SerialName("recovery_code") val code: String): app.upryzing.crescent.api.models.authentication.MFAResponse()
}