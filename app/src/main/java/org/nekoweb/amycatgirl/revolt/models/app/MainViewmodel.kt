package org.nekoweb.amycatgirl.revolt.models.app

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.nekoweb.amycatgirl.revolt.api.ApiClient
import org.nekoweb.amycatgirl.revolt.models.api.User
import org.nekoweb.amycatgirl.revolt.models.api.authentication.SessionResponse
import org.nekoweb.amycatgirl.revolt.models.websocket.BaseEvent
import org.nekoweb.amycatgirl.revolt.models.websocket.ReadyEvent
import org.nekoweb.amycatgirl.revolt.utilities.EventBus

class MainViewmodel : ViewModel() {
    val client = ApiClient
    private val listener = GlobalEventListener()
    var userList = mutableStateListOf<User>()
    var messageList = mutableStateListOf<BaseEvent>()
        private set

    init {
        viewModelScope.launch {
            val session: SessionResponse =
                client.loginWithPassword("raulytstation@protonmail.com", "Amyis_verycute")
            println(session)
            client.connectToWebsocket(listener)
            EventBus.subscribe<BaseEvent> { ev ->
                println("Got Message Event! $ev")
                messageList.add(ev)
            }
        }

        viewModelScope.launch {
            EventBus.subscribe<ReadyEvent> { ev ->
                userList.addAll(ev.users)
                ApiClient.cache.addAll(ev.users)
            }
        }
    }
}