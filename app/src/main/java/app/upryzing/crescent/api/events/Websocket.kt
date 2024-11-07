package app.upryzing.crescent.api.events

import android.util.Log
import app.upryzing.crescent.api.RevoltAPI
import app.upryzing.crescent.api.json.websocketDeserializer
import app.upryzing.crescent.api.models.websocket.BaseEvent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.receiveDeserialized
import io.ktor.client.plugins.websocket.wss
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.websocket.CloseReason
import io.ktor.websocket.close
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

class Websocket(private val client: RevoltAPI) {
    private val _events = MutableSharedFlow<Any>()
    val events = _events.asSharedFlow()

    private var _socket: DefaultClientWebSocketSession? = null

    private val ws = HttpClient(OkHttp) {
        install(WebSockets) {
            contentConverter = KotlinxWebsocketSerializationConverter(websocketDeserializer)
        }
    }

    private suspend fun publish(event: Any) {
        _events.emit(event)
    }

    suspend inline fun <reified T> subscribe(crossinline onEvent: (T) -> Unit) {
        events.filterIsInstance<T>()
            .collectLatest { event ->
                coroutineContext.ensureActive()
                onEvent(event)
            }
    }

    fun connect() {
        CoroutineScope(Dispatchers.IO).launch {
            ws.wss(client.connection.websocket) {
                _socket = this;

                try {
                    for (frame in incoming) {
                        val event: BaseEvent = receiveDeserialized()

                        Log.d("eventManager", "$event")

                        this@Websocket.publish(event)
                    }
                } catch(exception: Exception) {
                    Log.e("eventManager", "$exception")
                }
            }
        }
    }

    suspend fun disconnect() {
        _socket?.close(
            CloseReason(
                CloseReason.Codes.NORMAL,
                "We are disconnecting!"
            )
        )
    }
}