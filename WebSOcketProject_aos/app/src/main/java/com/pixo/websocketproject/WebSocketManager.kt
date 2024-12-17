package com.pixo.websocketproject

import android.util.Log
import okhttp3.*
import okio.ByteString

class WebSocketManager {
    private val client = OkHttpClient()
    private lateinit var webSocket: WebSocket

    fun connectWebSocket(url: String) {
        val request = Request.Builder().url(url).build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d("SocketLog","WebSocket 연결 성공!")
                // 서버로 메시지 전송
                webSocket.send("Hello, Server!")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("SocketLog","받은 메시지: $text")
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                Log.d("SocketLog","바이너리 메시지 수신: ${bytes.hex()}")
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Log.d("SocketLog","WebSocket 연결 종료: $code / $reason")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.d("SocketLog","WebSocket 에러: ${t.message}")
            }
        })
    }

    fun sendMessage(message: String) {
        webSocket.send(message)
    }

    fun closeWebSocket() {
        webSocket.close(1000, "Normal Closure")
    }
}
