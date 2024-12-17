package com.pixo.websocketproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {

    lateinit var wsManager: WebSocketManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        wsManager = WebSocketManager()

        val editTextView = findViewById<EditText>(R.id.editTextView)
        val connectingButton = findViewById<Button>(R.id.connectingButton)
        val sendButton = findViewById<Button>(R.id.sendButton)

        connectingButton.setOnClickListener {
            Log.d("SocketLog","socket Connection Button Click")
            wsManager.connectWebSocket("ws://localhost:8080/chat")
        }

        sendButton.setOnClickListener {
            val tmpString = editTextView.text.toString()

            tmpString?.let{
                Log.d("SocketLog","sendMessage : $it")
                wsManager.sendMessage("$it")
            }
        }
    }

}