package org.nekoweb.amycatgirl.revolt.models.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.nekoweb.amycatgirl.revolt.api.ApiClient
import org.nekoweb.amycatgirl.revolt.models.api.websocket.PartialMessage

class ChatViewmodel(
    id: String
) : ViewModel() {
    val messages = mutableStateListOf<PartialMessage>()

    init {
        viewModelScope.launch {
            messages.addAll(getMessages(id))
        }
    }
    suspend fun getMessages(channel: String): List<PartialMessage> {
        return ApiClient.getChannelMessages(channel).toMutableList()
    }
}