package app.upryzing.crescent.models.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.upryzing.crescent.api.ApiClient
import kotlinx.coroutines.launch

class ChatViewmodel(
    id: String
) : ViewModel() {
    val messages = mutableStateListOf<app.upryzing.crescent.api.models.websocket.PartialMessage>()

    init {
        messages.clear()

        viewModelScope.launch {
            messages.addAll(getMessages(id))
        }
    }
    suspend fun getMessages(channel: String): List<app.upryzing.crescent.api.models.websocket.PartialMessage> {
        return ApiClient.getChannelMessages(channel).toMutableList()
    }
}