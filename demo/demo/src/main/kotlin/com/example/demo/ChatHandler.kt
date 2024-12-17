package com.example.demo

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.web.socket.*
import org.springframework.web.socket.handler.TextWebSocketHandler

class ChatHandler : TextWebSocketHandler() {

    private val objectMapper = ObjectMapper() // Jackson ObjectMapper
    private val messageStorage = mutableListOf<ChatMessage>()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        println("클라이언트 연결됨: ${session.id}")
//        session.sendMessage(TextMessage("서버에 연결되었습니다."))
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        runCatching {
            val chatMessage = objectMapper.readValue(message.payload, ChatMessage::class.java)
            messageStorage.add(chatMessage)
            message.payload
        }.onSuccess { value ->
            // echo Message
            session.sendMessage(TextMessage(value))
        }.onFailure { exception ->
            println("error : $exception")
        }

    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        println("클라이언트 연결 종료: ${session.id}")
    }

    override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
        println("WebSocket 오류: ${exception.message}")
    }
}

data class ChatMessage(
    @JsonProperty("message")
    val message: String,       // 메시지 내용
    @JsonProperty("sender")
    val sender: String,        // 보낸 사람 (예: "user1" or "user2")
    @JsonProperty("timestamp")
    val timestamp: Long        // 서버에서 설정하는 시간
)
