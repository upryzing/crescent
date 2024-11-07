package app.upryzing.crescent.api.json

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

internal val websocketDeserializer = Json {
    ignoreUnknownKeys = true
    isLenient = true
    serializersModule = SerializersModule {
        polymorphic(app.upryzing.crescent.api.models.websocket.BaseEvent::class) {
            defaultDeserializer { app.upryzing.crescent.api.models.websocket.UnimplementedEvent.serializer() }
        }

        polymorphic(app.upryzing.crescent.api.models.websocket.SystemMessage::class) {
            defaultDeserializer { app.upryzing.crescent.api.models.websocket.SystemMessage.UnimplementedMessage.serializer() }
        }
    }
}