package com.pixo.websocketproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.annotations.SerializedName
import com.pixo.websocketproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var wsManager: WebSocketManager
    private val chatList = mutableListOf<ChatMessage>()
    private var userName = ""

    private lateinit var chatAdapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        wsManager = WebSocketManager()

        val serverActionListener: (chat: ChatMessage) -> Unit = { chat ->
            runOnUiThread {
                chat.isMine = chat.sender == userName
                chatList.add(chat)
                chatAdapter.updateData(chatList)
                chatAdapter.notifyItemInserted(chatList.lastIndex)
            }
        }

        with(binding) {
            userNameSaveButton.setOnClickListener {
                if(userNameEditTextView.text.isNotEmpty()) {
                    userName = userNameEditTextView.text.toString()
                    userNameEditTextView.isEnabled = false
                }
            }

            userNameEditButton.setOnClickListener {
                userNameEditTextView.isEnabled = true
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

            val recyclerView = binding.chatRecyclerView
            chatAdapter = ChatAdapter()
            chatAdapter.updateData(chatList)
            chatAdapter.notifyDataSetChanged()

            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            recyclerView.adapter = chatAdapter

        }
    }

}

data class ChatMessage(
    @SerializedName("message")
    val message: String,       // 채팅 내용
    @SerializedName("sender")
    val sender: String,        // 보낸 사람 (서버 or 사용자)
    @SerializedName("timestamp")
    val timestamp: Long,        // UNIX timestamp (밀리초)
    @Transient
    var isMine: Boolean = false
)
