package org.nekoweb.amycatgirl.revolt.models.app

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.nekoweb.amycatgirl.revolt.api.ApiClient
import org.nekoweb.amycatgirl.revolt.models.api.User
import org.nekoweb.amycatgirl.revolt.models.api.channels.Channel
import org.nekoweb.amycatgirl.revolt.models.api.websocket.ReadyEvent
import org.nekoweb.amycatgirl.revolt.utilities.EventBus

class HomeViewmodel : ViewModel() {
    val cache = mutableStateListOf<User>()
    val channelList = mutableStateListOf<Channel>()

    init {
        viewModelScope.launch {
            EventBus.subscribe<ReadyEvent> {
                viewModelScope.launch {
                    ApiClient.getDirectMessages().let { channels ->
                        channelList.clear()
                        channelList.addAll(channels)
                    }
                    cache.addAll(it.users)
                }
            }
        }
    }

}