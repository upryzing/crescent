package org.nekoweb.amycatgirl.revolt.models.app

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.nekoweb.amycatgirl.revolt.api.ApiClient
import org.nekoweb.amycatgirl.revolt.models.api.User
import org.nekoweb.amycatgirl.revolt.models.api.channels.Channel
import org.nekoweb.amycatgirl.revolt.models.api.websocket.ReadyEvent
import org.nekoweb.amycatgirl.revolt.utilities.EventBus
import javax.inject.Inject

@HiltViewModel
class HomeViewmodel @Inject constructor(
    client: ApiClient
) : ViewModel() {
    val cache = mutableStateListOf<User>()
    val channelList = mutableStateListOf<Channel>()

    init {
        viewModelScope.launch {
            EventBus.subscribe<ReadyEvent> {
                viewModelScope.launch {
                    cache.addAll(it.users)
                    client.getDirectMessages().let { channels ->
                        channelList.addAll(channels)
                    }
                }
            }
        }
    }
}