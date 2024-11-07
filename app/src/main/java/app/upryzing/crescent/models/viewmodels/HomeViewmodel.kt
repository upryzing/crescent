package app.upryzing.crescent.models.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.upryzing.crescent.api.ApiClient
import app.upryzing.crescent.utilities.EventBus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewmodel : ViewModel() {
    var channels = mutableStateListOf<app.upryzing.crescent.api.models.channels.Channel>()
    init {
        CoroutineScope(Dispatchers.IO).launch {
            EventBus.subscribe<app.upryzing.crescent.api.models.websocket.ReadyEvent> {
                viewModelScope.launch {
                    channels.addAll(fetchChannels())
                }
            }
        }
    }

    private suspend fun fetchChannels(): List<app.upryzing.crescent.api.models.channels.Channel> {
        return ApiClient.getDirectMessages()
    }
}