package app.upryzing.crescent.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json

class Raw(
    private val client: RevoltAPI
) {
    internal val http = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(JsonDeserialiser)
        }
        install(WebSockets) {
            contentConverter = KotlinxWebsocketSerializationConverter(JsonDeserialiser)
        }
    }

    internal suspend inline fun <reified T>get(route: String, noinline requestBuilder: HttpRequestBuilder.() -> Unit): T {
        return http.get(route, requestBuilder.apply {
            headers {
                if (client.session.currentSession != null) {
                    append("X-Session-Token", client.session.currentSession.userToken)
                }
            }
        }).body()
    }

    internal suspend inline fun <reified T>post(route: String, noinline requestBuilder: HttpRequestBuilder.() -> Unit): T {
        return http.post(route, requestBuilder.apply {
            headers {
                if (client.session.currentSession != null) {
                    append("X-Session-Token", client.session.currentSession.userToken)
                }
            }
        }).body()
    }

    internal suspend inline fun <reified T>patch(route: String, noinline requestBuilder: HttpRequestBuilder.() -> Unit): T {
        return http.patch(route, requestBuilder.apply {
            headers {
                if (client.session.currentSession != null) {
                    append("X-Session-Token", client.session.currentSession.userToken)
                }
            }
        }).body()
    }

    internal suspend inline fun <reified T>delete(route: String, noinline requestBuilder: HttpRequestBuilder.() -> Unit): T {
        return http.delete(route, requestBuilder.apply {
            headers {
                if (client.session.currentSession != null) {
                    append("X-Session-Token", client.session.currentSession.userToken)
                }
            }
        }).body()
    }
}