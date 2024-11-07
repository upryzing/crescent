package app.upryzing.crescent.api

import app.upryzing.crescent.models.api.authentication.EmailSessionRequest
import app.upryzing.crescent.models.api.authentication.SessionResponse
import io.ktor.client.request.accept
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class Session(private val client: RevoltAPI) {
    var currentSession: SessionResponse.Success? = null;

    suspend fun createSession(email: String, password: String): SessionResponse {
        val response = client.http.post<SessionResponse>("${client.options.apiUrl}/auth/session/login") {
            accept(ContentType.Application.Json)
            contentType(ContentType.Application.Json)

            setBody(EmailSessionRequest(email, password))
        }

        if (response is SessionResponse.Success) {
            currentSession = response
        }

        return response
    }
}