package org.nekoweb.amycatgirl.revolt.models.app

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import org.nekoweb.amycatgirl.revolt.api.ApiClient
import org.nekoweb.amycatgirl.revolt.models.api.websocket.PartialMessage

class ChatViewmodel : ViewModel() {
    val messages = mutableStateListOf<PartialMessage>()
    suspend fun getMessages(channel: String): List<PartialMessage> {
        println(channel)
        return ApiClient.getChannelMessages(channel)
    }
}