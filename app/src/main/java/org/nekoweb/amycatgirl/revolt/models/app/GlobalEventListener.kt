package org.nekoweb.amycatgirl.revolt.models.app

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.nekoweb.amycatgirl.revolt.models.api.websocket.BaseEvent
import org.nekoweb.amycatgirl.revolt.models.api.websocket.SocketListener
import org.nekoweb.amycatgirl.revolt.models.api.websocket.UnimplementedEvent

class GlobalEventListener : SocketListener {

    val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        serializersModule = SerializersModule {
            polymorphic(BaseEvent::class) {
                defaultDeserializer { UnimplementedEvent.serializer() }
            }
        }
    }
    override fun onConnect() {
        println("Connected!")
    }

    override fun onSocketMessage(raw: String) {
        println("EVENT")
        val decodedEvent: BaseEvent = json.decodeFromString(raw)
        println("I am $decodedEvent")
    }

    override fun onDisconnect(error: Exception?) {
        println("Disconnected from the websocket.")
        println("Error: $error")
    }
}