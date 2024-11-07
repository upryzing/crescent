package app.upryzing.crescent.api

import app.upryzing.crescent.models.api.authentication.SessionResponse

class Session(val client: RevoltAPI) {
    val currentSession: SessionResponse.Success? = null;

    fun createSession() {

    }
}