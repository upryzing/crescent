package app.upryzing.crescent.api

import app.upryzing.crescent.api.models.user.User
import io.ktor.client.call.body

class Users(private val client: RevoltAPI) : ClientCollection<User>() {
    suspend fun fetchUser(id: String): User {
        return client.http.get("users/$id").body()
    }
}