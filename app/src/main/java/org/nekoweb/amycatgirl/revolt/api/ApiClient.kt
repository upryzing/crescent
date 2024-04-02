package org.nekoweb.amycatgirl.revolt.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.wss
import io.ktor.serialization.kotlinx.json.json
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.nekoweb.amycatgirl.revolt.models.session.ClientSession
import org.nekoweb.amycatgirl.revolt.models.websocket.BaseEvent
import org.nekoweb.amycatgirl.revolt.models.websocket.SocketListener
import org.nekoweb.amycatgirl.revolt.models.websocket.UnimplementedEvent
import org.nekoweb.amycatgirl.revolt.utilities.EventBus

class ApiClient {
    companion object {
        const val API_ROOT_URL: String = "https://api.revolt.chat/"
        const val S3_ROOT_URL: String = "https://autumn.revolt.chat/"
        const val SOCKET_ROOT_URL: String = "wss://ws.revolt.chat?format=json&version=1&token=replace_me"
    }
    private val jsonDeserializer = Json {
        ignoreUnknownKeys = true
        isLenient = true
        serializersModule = SerializersModule {
            polymorphic(BaseEvent::class) {
                defaultDeserializer { UnimplementedEvent.serializer() }
            }
        }
    }

    lateinit var session: ClientSession
    val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(jsonDeserializer)
        }
        install(WebSockets)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun connectToWebsocket(listener: SocketListener) {
        GlobalScope.launch {
            client.wss(SOCKET_ROOT_URL) {
                listener.onConnect()

                try {
                    for (frame in incoming) {
                        if (frame is Frame.Text) {
                            val event: BaseEvent = jsonDeserializer.decodeFromString(frame.readText())
                            println("Deserialized frame (${frame.readText()}) as $event")
                            EventBus.publish(event)
                        }
                    }
                } catch (exception: Exception) {
                    listener.onDisconnect(exception)
                }
            }
        }
    }

    fun disconnectFromSocket() {
        client.close()
    }
}
