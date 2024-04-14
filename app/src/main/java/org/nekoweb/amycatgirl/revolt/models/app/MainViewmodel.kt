package org.nekoweb.amycatgirl.revolt.models.app

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.nekoweb.amycatgirl.revolt.api.ApiClient
import org.nekoweb.amycatgirl.revolt.models.api.websocket.BaseEvent
import org.nekoweb.amycatgirl.revolt.utilities.EventBus

class MainViewmodel : ViewModel() {
    var messageList = mutableStateListOf<BaseEvent>()
        private set

    init {
        viewModelScope.launch {
            ApiClient.connectToWebsocket()
            EventBus.subscribe<BaseEvent> { ev ->
                println("Got Message Event! $ev")
                messageList.add(ev)
            }
        }

    }
}