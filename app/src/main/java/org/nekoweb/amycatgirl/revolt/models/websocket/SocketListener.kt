package org.nekoweb.amycatgirl.revolt.models.websocket

interface SocketListener {
    fun onConnect()
    fun onSocketMessage(raw: String)
    fun onDisconnect(error: Exception?)
}