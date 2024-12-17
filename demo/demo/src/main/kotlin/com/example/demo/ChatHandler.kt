package com.example.demo

import org.springframework.web.socket.*
import org.springframework.web.socket.handler.TextWebSocketHandler

class ChatHandler : TextWebSocketHandler() {
    override fun afterConnectionEstablished(session: WebSocketSession) {
        println("클라이언트 연결됨: ${session.id}")
        session.sendMessage(TextMessage("서버에 연결되었습니다."))
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        println("받은 메시지: ${message.payload}")
        // Echo 메시지: 클라이언트로 받은 메시지를 다시 보냄
        session.sendMessage(TextMessage("서버에서 받은 메시지: ${message.payload}"))
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        println("클라이언트 연결 종료: ${session.id}")
    }

    override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
        println("WebSocket 오류: ${exception.message}")
    }
}
