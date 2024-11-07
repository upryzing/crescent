package app.upryzing.crescent.api

import app.upryzing.crescent.api.models.information.ApiInformation
import io.ktor.client.call.body
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RevoltAPI(options: ApiOptions? = null) {
    internal val options: ApiOptions = options ?: ApiOptions();
    lateinit var connection: ConnectionDetails;

    val session: Session = Session(this)

    val users: Users = Users(this)
    val channels: Channels = Channels(this)
    val messages: Messages = Messages()

    val http: Raw = Raw(this)

    var self: Self? = null

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val info = http.get("/").body<ApiInformation>()

            this@RevoltAPI.connection = ConnectionDetails(
                api = this@RevoltAPI.options.apiUrl,
                cdn = info.features?.cdn?.url,
                websocket = info.websocket,
                proxy = info.features?.proxy?.url,
                version = info.version
            )
        }
    }
}