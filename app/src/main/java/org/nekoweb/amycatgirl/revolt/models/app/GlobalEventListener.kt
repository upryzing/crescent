package org.nekoweb.amycatgirl.revolt.models.app

import kotlinx.serialization.json.Json
import org.nekoweb.amycatgirl.revolt.models.websocket.BaseEvent
import org.nekoweb.amycatgirl.revolt.models.websocket.SocketListener

class GlobalEventListener : SocketListener {

    val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
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