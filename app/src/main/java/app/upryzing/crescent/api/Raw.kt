package app.upryzing.crescent.api

import app.upryzing.crescent.api.json.apiDeserializer
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json

class Raw(
    private val client: RevoltAPI
) {
    internal val http = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(apiDeserializer)
        }
    }

    internal suspend fun get(route: String, requestBuilder: HttpRequestBuilder.() -> Unit = {}): HttpResponse {
        return http.get(client.options.apiUrl + route, requestBuilder.apply {
            headers {
                if (client.session.currentSession != null) {
                    append("X-Session-Token", client.session.currentSession!!.userToken)
                }
            }
        })
    }

    internal suspend fun post(route: String, requestBuilder: HttpRequestBuilder.() -> Unit = {}): HttpResponse {
        return http.post(client.options.apiUrl + route, requestBuilder.apply {
            headers {
                if (client.session.currentSession != null) {
                    append("X-Session-Token", client.session.currentSession!!.userToken)
                }
            }
        })
    }

    internal suspend fun patch(route: String, requestBuilder: HttpRequestBuilder.() -> Unit = {}): HttpResponse {
        return http.patch(client.options.apiUrl + route, requestBuilder.apply {
            headers {
                if (client.session.currentSession != null) {
                    append("X-Session-Token", client.session.currentSession!!.userToken)
                }
            }
        }).body()
    }

    internal suspend fun delete(route: String, requestBuilder: HttpRequestBuilder.() -> Unit = {}): HttpResponse {
        return http.delete(client.options.apiUrl + route, requestBuilder.apply {
            headers {
                if (client.session.currentSession != null) {
                    append("X-Session-Token", client.session.currentSession!!.userToken)
                }
            }
        }).body()
    }
}