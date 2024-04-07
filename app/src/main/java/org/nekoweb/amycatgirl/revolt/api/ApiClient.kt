package org.nekoweb.amycatgirl.revolt.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.wss
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.nekoweb.amycatgirl.revolt.models.api.PartialMessage
import org.nekoweb.amycatgirl.revolt.models.api.authentication.SessionRequestWithFriendlyName
import org.nekoweb.amycatgirl.revolt.models.api.authentication.SessionResponse
import org.nekoweb.amycatgirl.revolt.models.api.channels.Channel
import org.nekoweb.amycatgirl.revolt.models.websocket.BaseEvent
import org.nekoweb.amycatgirl.revolt.models.websocket.SocketListener
import org.nekoweb.amycatgirl.revolt.models.websocket.UnimplementedEvent
import org.nekoweb.amycatgirl.revolt.utilities.EventBus

object ApiClient {
        private const val DEBUG_TOKEN =
            "cuB2i01f-IGdGb8amCHKZ1QuycKx1xPkPsoKdNjFhdgFeYOtQz5e0_331B1KyGIL"
        const val API_ROOT_URL: String = "https://api.revolt.chat/"
        const val S3_ROOT_URL: String = "https://autumn.revolt.chat/"
        const val SOCKET_ROOT_URL: String =
            "wss://ws.revolt.chat?format=json&version=1&token=$DEBUG_TOKEN"

        var currentSession: SessionResponse? = null

    private val jsonDeserializer = Json {
        ignoreUnknownKeys = true
        isLenient = true
        serializersModule = SerializersModule {
            polymorphic(BaseEvent::class) {
                defaultDeserializer { UnimplementedEvent.serializer() }
            }
        }
    }

    private val client = HttpClient(OkHttp) {
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
                            val event: BaseEvent =
                                jsonDeserializer.decodeFromString(frame.readText())
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

    suspend fun getDirectMessages(): List<Channel> {
        return client.get("$API_ROOT_URL/users/dms") {
            headers {
                append("X-Session-Token", DEBUG_TOKEN)
            }

            accept(ContentType.Application.Json)
        }.body<List<Channel>>()
    }

    suspend fun getSpecificMessageFromChannel(
        channel: Channel,
        messageId: String
    ): PartialMessage? {
        return try {
            println("Requesting $messageId from ${channel.id}")
            val res = client.get("$API_ROOT_URL/channel/${channel.id}/messages/${messageId}") {
                headers {
                    append("X-Session-Token", DEBUG_TOKEN)
                }

                accept(ContentType.Application.Json)
            }

            println("Got result: ${res.bodyAsText()}")
            res.body<PartialMessage>()
        } catch (e: Exception) {
            println("Fuck, $e")
            null
        }
    }

    suspend fun getChannelMessages(channel: Channel) {
        val res = client.get("$API_ROOT_URL/channel/${channel.id}/messages?limit=30") {
            headers {
                append("X-Session-Token", DEBUG_TOKEN)
            }

            accept(ContentType.Application.Json)
        }

        println("Got result: ${res.bodyAsText()}")
        res.body<List<PartialMessage>>()
    }

    suspend fun loginWithPassword(email: String, password: String): SessionResponse {
        val response = client.post("$API_ROOT_URL/auth/session/login") {
            accept(ContentType.Application.Json)
            contentType(ContentType.Application.Json)

            setBody(SessionRequestWithFriendlyName(email, password))
        }.body<SessionResponse>()

        currentSession = response

        return response
    }

    fun disconnectFromSocket() {
        client.close()
    }
}
