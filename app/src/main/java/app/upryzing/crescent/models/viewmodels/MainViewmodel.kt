package app.upryzing.crescent.models.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.upryzing.crescent.api.ApiClient
import app.upryzing.crescent.api.ConnectionDetails
import app.upryzing.crescent.api.RevoltAPI
import app.upryzing.crescent.utilities.EventBus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewmodel @Inject constructor(
    val clientNext: RevoltAPI
) : ViewModel() {
    var messageList = mutableStateListOf<Any>()
        private set

    fun getClientInformation(): ConnectionDetails? {
        return clientNext.connection
    }

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