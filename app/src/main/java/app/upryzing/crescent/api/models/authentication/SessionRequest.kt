package app.upryzing.crescent.api.models.authentication

import android.os.Build.VERSION.RELEASE
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed class SessionRequest {
    @Serializable
    data class MultiFactor(
        @SerialName("mfa_ticket") val ticket: String,
        @SerialName("mfa_response") val response: app.upryzing.crescent.api.models.authentication.MFAResponse,
        @SerialName("friendly_name") val friendlyName: String = "Crescent on Android $RELEASE"
    ) : app.upryzing.crescent.api.models.authentication.SessionRequest()

    @Serializable
    data class Email(
        val email: String,
        val password: String,
        @SerialName("friendly_name") val friendlyName: String = "Crescent on Android $RELEASE"
    ): app.upryzing.crescent.api.models.authentication.SessionRequest()
}
