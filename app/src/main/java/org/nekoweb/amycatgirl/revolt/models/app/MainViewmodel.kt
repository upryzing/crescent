package org.nekoweb.amycatgirl.revolt.models.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.nekoweb.amycatgirl.revolt.api.ApiClient

@OptIn(DelicateCoroutinesApi::class)
class MainViewmodel : ViewModel() {
    private val client = ApiClient()
    private val listener = GlobalEventListener()
    init {
        viewModelScope.launch {
            client.connectToWebsocket(listener)
        }
    }
}