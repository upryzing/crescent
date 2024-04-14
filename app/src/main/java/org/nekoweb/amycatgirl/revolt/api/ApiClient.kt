package org.nekoweb.amycatgirl.revolt.api

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.receiveDeserialized
import io.ktor.client.plugins.websocket.wss
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import io.ktor.websocket.DefaultWebSocketSession
import io.ktor.websocket.Frame
import io.ktor.websocket.send
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.nekoweb.amycatgirl.revolt.models.api.authentication.SessionRequestWithFriendlyName
import org.nekoweb.amycatgirl.revolt.models.api.authentication.SessionResponse
import org.nekoweb.amycatgirl.revolt.models.api.channels.Channel
import org.nekoweb.amycatgirl.revolt.models.api.websocket.AuthenticateEvent
import org.nekoweb.amycatgirl.revolt.models.api.websocket.BaseEvent
import org.nekoweb.amycatgirl.revolt.models.api.websocket.PartialMessage
import org.nekoweb.amycatgirl.revolt.models.api.websocket.UnimplementedEvent
import org.nekoweb.amycatgirl.revolt.utilities.EventBus

object ApiClient {
    var useStaging = false
        set(value) {
            when (value) {
                true -> {
                    API_ROOT_URL = "https://revolt.chat/api/"
                    SOCKET_ROOT_URL = "wss://revolt.chat/events/?format=json&version=1"
                }

                false -> {
                    API_ROOT_URL = "https://api.revolt.chat"
                    SOCKET_ROOT_URL = "wss://ws.revolt.chat?format=json&version=1"

                }
            }
            field = value
        }
    private var SOCKET_ROOT_URL: String = "wss://ws.revolt.chat?format=json&version=1"
    private var API_ROOT_URL: String = "https://api.revolt.chat/"
    const val S3_ROOT_URL: String = "https://autumn.revolt.chat/"

    private var currentSession: SessionResponse.Success? = null
    private var websocket: DefaultWebSocketSession? = null
    private val jsonDeserializer = Json {
        ignoreUnknownKeys = true
        isLenient = true
        serializersModule = SerializersModule {
            polymorphic(BaseEvent::class) {
                defaultDeserializer { UnimplementedEvent.serializer() }
            }
        }
    }

    var cache = mutableListOf<Any>()

    private val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(jsonDeserializer)
        }
        install(WebSockets) {
            contentConverter = KotlinxWebsocketSerializationConverter(jsonDeserializer)
        }
    }

    fun connectToWebsocket() {
        CoroutineScope(Dispatchers.IO).launch {
            client.wss(SOCKET_ROOT_URL) {
                websocket = this@wss

                try {
                    for (frame in incoming) {
                        if (frame is Frame.Text) {
                            val event: BaseEvent = receiveDeserialized()
                            Log.d("Socket", "Got Event: $event")
                            EventBus.publish(event)
                        }
                    }
                } catch (exception: Exception) {
                    Log.e("Socket", "$exception")
                }
            }
        }
    }

    suspend fun getDirectMessages(): List<Channel> {
        println("Getting Direct Messages...")
        val res = client.get("$API_ROOT_URL/users/dms") {
            headers {
                append("X-Session-Token", currentSession?.userToken ?: "")
            }

            accept(ContentType.Application.Json)
        }.body<List<Channel>>()

        cache.addAll(res)
        println("Result: $res")
        println("Cache size: ${cache.size}")
        return res
    }

    suspend fun getSpecificMessageFromChannel(
        channel: Channel, messageId: String
    ): PartialMessage? {
        return try {
            println("Requesting $messageId from ${channel.id}")
            val res = client.get("$API_ROOT_URL/channel/${channel.id}/messages/${messageId}") {
                headers {
                    append("X-Session-Token", currentSession?.userToken ?: "")
                }

                accept(ContentType.Application.Json)
            }.body<PartialMessage>()

            cache.add(res)
            println("Cache size: ${cache.size}")
            return res
        } catch (e: Exception) {
            println("Fuck, $e")
            null
        }
    }

    suspend fun getChannelMessages(user: String): List<PartialMessage> {
        val channel =
            cache.filterIsInstance<Channel.DirectMessage>().find { it.recipients.contains(user) }
                ?: cache.filterIsInstance<Channel.Group>().find { it.id == user }
        println("Found Channel: $channel")
        val url = "${API_ROOT_URL}channels/${channel?.id}/messages?limit=30"
        println("requesting messages from ")
        val res = client.get(url) {
            headers {
                append("X-Session-Token", currentSession?.userToken ?: "")
            }

            accept(ContentType.Application.Json)
        }.body<List<PartialMessage>>()

        cache.addAll(res)
        println("Cache size: ${cache.size}")

        return res
    }

    suspend fun sendMessage(location: String, message: String) {
        val channel = cache.filterIsInstance<Channel.DirectMessage>()
            .find { it.recipients.contains(location) } ?: cache.filterIsInstance<Channel.Group>()
            .find { it.id == location }
        val url = "${API_ROOT_URL}channels/${channel?.id}/messages"
        client.post(url) {
            headers {
                append("X-Session-Token", currentSession?.userToken ?: "")
            }

            contentType(ContentType.Application.Json)
            setBody(PartialMessage(content = message))
        }
    }

    suspend fun loginWithPassword(email: String, password: String): SessionResponse {
        val response = client.post("$API_ROOT_URL/auth/session/login") {
            accept(ContentType.Application.Json)
            contentType(ContentType.Application.Json)

            setBody(SessionRequestWithFriendlyName(email, password))
        }.body<SessionResponse>()

        if (response is SessionResponse.Success) {
            currentSession = response
            websocket?.send(
                jsonDeserializer.encodeToString(
                    AuthenticateEvent(
                        "Authenticate",
                        response.userToken
                    )
                )
            )
        }

        return response
    }

    fun closeSession() {
        client.close()
    }
}
