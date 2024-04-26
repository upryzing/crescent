package org.nekoweb.amycatgirl.revolt.models.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.nekoweb.amycatgirl.revolt.api.ApiClient
import org.nekoweb.amycatgirl.revolt.models.api.channels.Channel
import org.nekoweb.amycatgirl.revolt.models.api.websocket.ReadyEvent
import org.nekoweb.amycatgirl.revolt.utilities.EventBus

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