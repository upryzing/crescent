package app.upryzing.crescent.api

import android.util.Log
import app.upryzing.crescent.api.models.authentication.*
import app.upryzing.crescent.api.models.user.User
import io.ktor.client.call.body
import io.ktor.client.request.accept
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class Session(private val client: RevoltAPI) {
    var currentSession: SessionResponse.Success? = null

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

            Log.d("API", "Current session token: ${currentSession?.userToken}")


            populateSelf()
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

    private suspend fun populateSelf() {
        val response = client.http.get("users/@me")

        val rawResponse = response.bodyAsText()
        val headers = response.request.headers

        Log.d("API", "got user: $rawResponse")
        Log.d("API", "used headers: $headers")

        val selfUser = response.body<User>()

        client.self = Self(
            selfUser,
            client
        )
    }
}