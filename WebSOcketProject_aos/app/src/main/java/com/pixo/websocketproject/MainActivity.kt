package com.pixo.websocketproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {

    private lateinit var wsManager: WebSocketManager
    private val chatList = mutableListOf<ChatMessage>()
    private var userName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        wsManager = WebSocketManager()

        val userNameEditText = findViewById<EditText>(R.id.userNameEditTextView)
        val userNameSaveButton = findViewById<Button>(R.id.userNameSaveButton)
        val userNameEditButton = findViewById<Button>(R.id.userNameEditButton)

        val messageEditTextView = findViewById<EditText>(R.id.messageEditTextView)
        val connectingButton = findViewById<Button>(R.id.connectingButton)
        val sendButton = findViewById<Button>(R.id.sendButton)

        val serverActionListener: (message: ChatMessage) -> Unit = { chat ->
            chatList.add(chat)
        }

        userNameSaveButton.setOnClickListener {
            if(userNameEditText.text.isNotEmpty()) {
                userName = userNameEditText.text.toString()
                userNameEditText.isEnabled = false
            }
        }

        userNameEditButton.setOnClickListener {
            userNameEditText.isEnabled = true
        }

        connectingButton.setOnClickListener {
            wsManager.connectWebSocket("ws://10.0.2.2:8080/chat", serverActionListener)
        }

        sendButton.setOnClickListener {
            val message = messageEditTextView.text.toString()

            if(message.isNotEmpty() && userName.isNotEmpty()) {
                val chatMessage = ChatMessage(message, userName, System.currentTimeMillis())
                wsManager.sendChatMessage(chatMessage)
            }
        }
    }

}

data class ChatMessage(
    val message: String,       // 채팅 내용
    val sender: String,        // 보낸 사람 (서버 or 사용자)
    val timestamp: Long        // UNIX timestamp (밀리초)
)
