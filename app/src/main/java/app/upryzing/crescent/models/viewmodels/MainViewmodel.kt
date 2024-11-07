package app.upryzing.crescent.models.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.upryzing.crescent.api.ApiClient
import app.upryzing.crescent.utilities.EventBus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewmodel : ViewModel() {
    var messageList = mutableStateListOf<Any>()
        private set

    init {
        viewModelScope.launch {
            EventBus.subscribe<Any> { ev ->
                Log.d("EventBus", "$ev")
                messageList.add(ev)
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            EventBus.subscribe<app.upryzing.crescent.api.models.websocket.ReadyEvent> { event ->
                event.users.forEach {
                    ApiClient.cache[it.id] = it
                }
            }
        }

    }
}