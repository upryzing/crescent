package app.upryzing.crescent.models.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.upryzing.crescent.api.ApiClient
import app.upryzing.crescent.models.api.websocket.PartialMessage
import kotlinx.coroutines.launch

class ChatViewmodel(
    id: String
) : ViewModel() {
    val messages = mutableStateListOf<PartialMessage>()

    init {
        messages.clear()

        viewModelScope.launch {
            messages.addAll(getMessages(id))
        }
    }
    suspend fun getMessages(channel: String): List<PartialMessage> {
        return ApiClient.getChannelMessages(channel).toMutableList()
    }
}