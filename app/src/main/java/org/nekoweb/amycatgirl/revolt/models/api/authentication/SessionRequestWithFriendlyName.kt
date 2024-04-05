package org.nekoweb.amycatgirl.revolt.models.api.authentication

import android.os.Build.VERSION.RELEASE
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SessionRequestWithFriendlyName(
    val email: String,
    val password: String,
    @SerialName("friendly_name")
    val friendlyName: String = "RevoltMini on Android $RELEASE"
)
