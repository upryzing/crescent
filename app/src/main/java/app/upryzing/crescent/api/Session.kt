package app.upryzing.crescent.api

import app.upryzing.crescent.api.models.authentication.*
import app.upryzing.crescent.api.models.user.User
import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class Session(private val client: RevoltAPI) {
    var currentSession: SessionResponse.Success? = null;

    /**
     * Create a new client session, fails if a session already exists.
     */
    suspend fun createSession(req: SessionRequest): SessionResponse {
        require(this.currentSession == null)

        val response = client.http.post("auth/session/login") {
            accept(ContentType.Application.Json)
            contentType(ContentType.Application.Json)

            setBody(req)
        }.body<SessionResponse>()

        if (response is SessionResponse.Success) {
            currentSession = response

            client.self = client.http.get("users/@me").body<User>()
        }

        return response
    }


    /**
     * Revokes the client's current session
     */
    suspend fun dropSession() {
        require(currentSession != null)

        client.http.delete("auth/session/${currentSession!!.id}")

        // Assume session has been dropped, remove current session and self user
        currentSession = null
        client.self = null
    }
}