package org.nekoweb.amycatgirl.revolt.models.app

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.launch
import org.nekoweb.amycatgirl.revolt.api.ApiClient
import org.nekoweb.amycatgirl.revolt.models.api.websocket.ReadyEvent
import org.nekoweb.amycatgirl.revolt.utilities.EventBus

class MainViewmodel : ViewModel() {
    var messageList = mutableStateListOf<Any>()
        private set

    init {
        viewModelScope.launch {
            ApiClient.connectToWebsocket()
            EventBus.subscribe<Any> { ev ->
                Log.d("EventBus", "$ev")
                messageList.add(ev)
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            EventBus.subscribe<ReadyEvent> {
                ApiClient.cache.addAll(it.users)
            }
        }

    }
}