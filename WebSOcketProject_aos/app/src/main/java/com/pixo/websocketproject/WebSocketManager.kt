package com.pixo.websocketproject

import android.util.Log
import com.google.gson.Gson
import okhttp3.*
import okio.ByteString

class WebSocketManager {

    interface ConnectionListener {
        fun onConnected()
        fun onDisconnected()
        fun onError(error: String)
    }

    private var connectionListener: ConnectionListener? = null

    private val client = OkHttpClient()
    private lateinit var webSocket: WebSocket

    private val gson = Gson()

    fun setConnectionListener(listener: ConnectionListener) {
        connectionListener = listener
    }

    fun connectWebSocket(url: String, listener: (chat: ChatMessage) -> Unit) {
        val request = Request.Builder().url(url).build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                connectionListener?.onConnected()
                Log.d("SocketLog","WebSocket 연결 성공!")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                val chatMessage = gson.fromJson(text, ChatMessage::class.java)
                listener(chatMessage)
                Log.d("SocketLog","받은 메시지: $text")
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                Log.d("SocketLog","바이너리 메시지 수신: ${bytes.hex()}")
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                connectionListener?.onDisconnected()
                Log.d("SocketLog","WebSocket 연결 종료: $code / $reason")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                connectionListener?.onError(t.message ?: "Unknown error")
                Log.d("SocketLog","WebSocket 에러: ${t.message}")
            }
        })
    }

    fun sendChatMessage(chatMessage: ChatMessage) {
        val jsonChatMessage = gson.toJson(chatMessage)
        Log.d("SocketLog","chatMessage in Manager : $jsonChatMessage")
        webSocket.send(jsonChatMessage)
    }

    fun closeWebSocket() {
        webSocket.close(1000, "Normal Closure")
    }
}
