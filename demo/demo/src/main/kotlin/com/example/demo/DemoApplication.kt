package com.example.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WebSocketApplication

fun main(args: Array<String>) {
	runApplication<WebSocketApplication>(*args)
	println("WebSocket 서버가 ws://localhost:8080/chat 에서 실행 중입니다.")
}
