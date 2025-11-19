package com.example.emojiguess.ui.chat

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.emojiguess.R
import com.example.emojiguess.databinding.ItemMessageBinding
import com.example.emojiguess.models.Message
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatAdapter(private val currentPlayerId: String) :
    ListAdapter<Message, ChatAdapter.MessageViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMessageBinding.inflate(inflater, parent, false)
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(getItem(position), currentPlayerId)
    }

    class MessageViewHolder(private val binding: ItemMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: Message, currentPlayerId: String) {
            val isMine = message.senderId == currentPlayerId
            binding.textSenderName.text = if (isMine) binding.root.context.getString(R.string.you_label) else message.senderName
            binding.textMessageContent.text = message.content
            binding.textTimestamp.text = FORMAT.format(Date(message.timestamp))

            val params = binding.cardMessage.layoutParams
            if (params is FrameLayout.LayoutParams) {
                params.gravity = if (isMine) Gravity.END else Gravity.START
                binding.cardMessage.layoutParams = params
            }
            val context = binding.cardMessage.context
            val backgroundColor = if (isMine) {
                ContextCompat.getColor(context, R.color.chatBubbleSelf)
            } else {
                ContextCompat.getColor(context, R.color.chatBubbleOther)
            }
            binding.cardMessage.setCardBackgroundColor(backgroundColor)
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean = oldItem == newItem
    }

    companion object {
        private val FORMAT = SimpleDateFormat("HH:mm", Locale.getDefault())
    }
}