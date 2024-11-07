package app.upryzing.crescent.api.models.information

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Build(
    @SerialName("commit_sha") val commit: String,
    @SerialName("commit_timestamp") val commitedSince: String,
    @SerialName("semver") val version: String,
    @SerialName("origin_url") val git: String,
    @SerialName("timestamp") val builtSince: String
)
