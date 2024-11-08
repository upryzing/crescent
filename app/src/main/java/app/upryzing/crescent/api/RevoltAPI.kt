package app.upryzing.crescent.api

import android.util.Log
import app.upryzing.crescent.api.events.Websocket
import app.upryzing.crescent.api.models.information.ApiInformation
import io.ktor.client.call.body
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RevoltAPI(internal val options: ApiOptions = ApiOptions()) {
    var connection: ConnectionDetails? = null

    val session: Session = Session(this)

    val users: Users = Users(this)
    val channels: Channels = Channels(this)
    val messages: Messages = Messages()

    val http: Raw = Raw(this)
    val ws: Websocket = Websocket(this)

    var self: Self? = null

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val info = http.get("/").body<ApiInformation>()

            Log.d("API", "Got API Information: $info")

            this@RevoltAPI.connection = ConnectionDetails(
                api = this@RevoltAPI.options.apiUrl,
                cdn = info.features?.cdn?.url,
                websocket = info.websocket,
                proxy = info.features?.proxy?.url,
                version = info.version
            )

            Log.d("API", "Init done, Client can now be used!")
        }
    }
}