package com.example.demo

import org.springframework.web.socket.*
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.time.Instant

class ChatHandler : TextWebSocketHandler() {
    override fun afterConnectionEstablished(session: WebSocketSession) {
        println("클라이언트 연결됨: ${session.id}")
//        session.sendMessage(TextMessage("서버에 연결되었습니다."))
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        println("받은 메시지: ${message.payload}")

        // 현재 시간 가져오기 (timestamp: UNIX 밀리초)
        val currentTimestamp = Instant.now().toEpochMilli()

        // 서버에서 메시지를 다시 만들어 보냄
        val responseMessage = ChatMessage(
            message = "sending success",
            sender = "SERVER",
            timestamp = currentTimestamp
        )

        // JSON 형식으로 메시지를 보냄
        val jsonResponse = """
            {"message": "${responseMessage.message}", "sender": "${responseMessage.sender}", "timestamp": ${responseMessage.timestamp}}
        """.trimIndent()

        session.sendMessage(TextMessage(jsonResponse))
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        println("클라이언트 연결 종료: ${session.id}")
    }

    override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
        println("WebSocket 오류: ${exception.message}")
    }
}

data class ChatMessage(
    val message: String,       // 메시지 내용
    val sender: String,        // 보낸 사람 (예: "user1" or "user2")
    val timestamp: Long        // 서버에서 설정하는 시간
)
