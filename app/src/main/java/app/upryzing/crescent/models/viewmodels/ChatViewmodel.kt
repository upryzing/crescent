package app.upryzing.crescent.models.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.upryzing.crescent.api.ApiClient
import app.upryzing.crescent.models.api.websocket.PartialMessage
import kotlinx.coroutines.launch

class ChatViewmodel(
    // The initial ID for the chat when the ViewModel is first created.
    // This can be an empty string or a specific default if desired.
    initialId: String
) : ViewModel() {
    val messages = mutableStateListOf<PartialMessage>()
    private var currentChatId: String = initialId

    init {
        // Load messages for the initial ID if provided
        if (initialId.isNotBlank()) {
            setCurrentChatId(initialId)
        }
    }

    /**
     * Sets the current chat ID for the ViewModel and loads its messages.
     * This will clear any existing messages before loading new ones.
     *
     * @param newUlid The ULID of the chat to load.
     */
    fun setCurrentChatId(newUlid: String) {
        currentChatId = newUlid
        messages.clear()
        viewModelScope.launch {
            messages.addAll(getMessages(newUlid))
        }
    }

    /**
     * Fetches messages for a given channel ID.
     * This is a suspend function as it involves an API call.
     *
     * @param channel The ID of the channel to fetch messages from.
     * @return A list of PartialMessage objects.
     */
    suspend fun getMessages(channel: String): List<PartialMessage> {
        return ApiClient.getChannelMessages(channel).toMutableList()
    }
}