package org.nekoweb.amycatgirl.revolt.models.api.authentication

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("result")
sealed class SessionResponse {
    abstract val result: String

    @SerialName("Success")
    data class Success(
        override val result: String,
        @SerialName("_id") val id: String,
        @SerialName("user_id") val userId: String,
        @SerialName("token") val userToken: String,
        @SerialName("name") val sessionName: String
    ) : SessionResponse()

    @SerialName("MFA")
    data class NeedsMultiFactorAuth(
        override val result: String,
        val ticket: String,
        @SerialName("allowed_methods") val methods: List<String>
    ) : SessionResponse()

    @SerialName("Disabled")
    data class AccountDisabled(
        override val result: String,
        @SerialName("user_id") val disabledUserId: String
    ) : SessionResponse()
}