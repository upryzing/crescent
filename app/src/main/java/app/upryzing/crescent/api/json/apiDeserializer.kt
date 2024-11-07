package app.upryzing.crescent.api.json

import kotlinx.serialization.json.Json

internal val apiDeserializer = Json {
    ignoreUnknownKeys = true
    isLenient = true
}