package com.example.chatgptopensource.adapters

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.chatgptopensource.R
import com.example.chatgptopensource.apiCall.Message
import com.example.chatgptopensource.db.MessageEntity

class HistoryAdapter() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var listener: OnClickListener? = null
    private var messages: List<MessageEntity> = ArrayList()

    companion object {
        const val USER_MESSAGE_TYPE = 1
        const val GPT_MESSAGE_TYPE = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            USER_MESSAGE_TYPE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_user_message, parent, false)
                UserMessageViewHolder(view)
            }

            GPT_MESSAGE_TYPE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_gpt_message, parent, false)
                GptMessageViewHolder(view)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val chat = messages[position]
        when (holder) {
            is UserMessageViewHolder -> {
                holder.bindUserMessage(chat.content)
            }

            is GptMessageViewHolder -> {
                holder.bindGptMessage(chat.content)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val chat = messages[position]
        return if (chat.role == "user") {
            USER_MESSAGE_TYPE
        } else {
            GPT_MESSAGE_TYPE
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    inner class UserMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userMessageTextView: TextView = itemView.findViewById(R.id.userMessageTextView)

        fun bindUserMessage(message: String) {
            userMessageTextView.text = message
        }
    }

    inner class GptMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val gptMessageTextView: TextView = itemView.findViewById(R.id.gptMessageTextView)
        private val gptCopyImageView: ImageView = itemView.findViewById(R.id.imgCopy)
        private val gptListenMessage: ImageView = itemView.findViewById(R.id.imgListen)

        fun bindGptMessage(message: String) {
            gptMessageTextView.text = message

            gptListenMessage.setOnClickListener {
                listener?.onItemClick(message)
            }

            gptCopyImageView.setOnClickListener {
                val clipboardManager =
                    itemView.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("Chat AI", message)
                clipboardManager.setPrimaryClip(clipData)
                Toast.makeText(itemView.context,
                    itemView.context.getString(R.string.text_copied_to_clipboard), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    interface OnClickListener {
        fun onItemClick(message: String)
    }

    fun setOnClickListener(listener: OnClickListener) {
        this.listener = listener
    }

    fun submitList(list: List<MessageEntity>)
    {
        this.messages = list
        notifyDataSetChanged()
    }
}