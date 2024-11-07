package app.upryzing.crescent.api

import app.upryzing.crescent.models.api.websocket.BaseEvent
import app.upryzing.crescent.models.api.websocket.SystemMessage
import app.upryzing.crescent.models.api.websocket.UnimplementedEvent
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

internal val JsonDeserialiser = Json {
    ignoreUnknownKeys = true
    isLenient = true
    serializersModule = SerializersModule {
        polymorphic(BaseEvent::class) {
            defaultDeserializer { UnimplementedEvent.serializer() }
        }

        polymorphic(SystemMessage::class) {
            defaultDeserializer { SystemMessage.UnimplementedMessage.serializer() }
        }
    }
}