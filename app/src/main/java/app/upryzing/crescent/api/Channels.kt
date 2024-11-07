package app.upryzing.crescent.api

import app.upryzing.crescent.api.models.channels.Channel
import io.ktor.client.call.body

class Channels(private val client: RevoltAPI) : ClientCollection<Channel>() {
    suspend fun fetchChannel(id: String): Channel {
        return client.http.get("channels/$id").body<Channel>()
    }
}