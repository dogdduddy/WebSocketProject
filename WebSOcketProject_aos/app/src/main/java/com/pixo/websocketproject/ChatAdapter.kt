package com.pixo.websocketproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var chatList: List<ChatMessage> = listOf<ChatMessage>()

    companion object {
        private const val VIEW_TYPE_ME = 1
        private const val VIEW_TYPE_OTHER = 2
    }

    // 메시지의 타입을 결정
    override fun getItemViewType(position: Int): Int {
        return if (chatList[position].isMine) VIEW_TYPE_ME else VIEW_TYPE_OTHER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ME -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_chat_me, parent, false)
                MeViewHolder(view)
            }
            VIEW_TYPE_OTHER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_chat_other, parent, false)
                OtherViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = chatList[position]
        when (holder) {
            is MeViewHolder -> holder.bind(message)
            is OtherViewHolder -> holder.bind(message)
        }
    }

    override fun getItemCount(): Int = chatList.size

    // 내 채팅 ViewHolder
    class MeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvMessage: TextView = itemView.findViewById(R.id.tvMessageMe)
        fun bind(message: ChatMessage) {
            tvMessage.text = message.message
        }
    }

    // 다른 사람 채팅 ViewHolder
    class OtherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvMessage: TextView = itemView.findViewById(R.id.tvMessageOther)
        fun bind(message: ChatMessage) {
            tvMessage.text = message.message
        }
    }

    fun updateData(newChatList: List<ChatMessage>) {
        val diffCallback = ChatDiffCallback(chatList, newChatList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        chatList = newChatList
        diffResult.dispatchUpdatesTo(this)
    }
}


class ChatDiffCallback(
    private val oldList: List<ChatMessage>,
    private val newList: List<ChatMessage>
) : DiffUtil.Callback() {

    // 두 리스트의 크기 비교
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    // 두 아이템이 동일한지 비교 (ID나 고유 값 기준)
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].timestamp == newList[newItemPosition].timestamp
    }

    // 내용이 동일한지 비교
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}