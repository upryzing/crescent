package app.upryzing.crescent.models.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.upryzing.crescent.api.ApiClient
import app.upryzing.crescent.models.api.channels.Channel
import app.upryzing.crescent.models.api.websocket.ReadyEvent
import app.upryzing.crescent.utilities.EventBus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewmodel : ViewModel() {
    var channels = mutableStateListOf<Channel>()
    init {
        CoroutineScope(Dispatchers.IO).launch {
            EventBus.subscribe<ReadyEvent> {
                viewModelScope.launch {
                    channels.addAll(fetchChannels())
                }
            }
        }
    }

    private suspend fun fetchChannels(): List<Channel> {
        return ApiClient.getDirectMessages()
    }
}