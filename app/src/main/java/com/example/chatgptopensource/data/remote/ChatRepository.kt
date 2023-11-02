package com.example.chatgptopensource.data.remote

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.chatgptopensource.apiCall.ChatCompletionResponse
import com.example.chatgptopensource.apiCall.RequestBodyObject
import com.example.chatgptopensource.apiCall.RetrofitClient
import com.example.chatgptopensource.db.ChatDB
import com.example.chatgptopensource.db.MessageEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatRepository {
    private val apiService = RetrofitClient.create()
    private var messageList: LiveData<MutableList<MessageEntity>>? = null
    private var instanceOfRepository: ChatDB? = null


    suspend fun getChatCompletion(requestBody: RequestBodyObject): ChatCompletionResponse {
        return apiService.getChatCompletion(requestBody)
    }

    private fun initializeDB(context: Context) : ChatDB {
        return ChatDB.getDatabaseClient(context)
    }

    fun insertData(context: Context, model: MessageEntity) {
        instanceOfRepository = initializeDB(context)
        CoroutineScope(Dispatchers.IO).launch {
            instanceOfRepository!!.messageDAO().insertMessage(model)
        }
    }

    fun getAllMessages(context: Context) : LiveData<MutableList<MessageEntity>>? {
        instanceOfRepository = initializeDB(context)
        messageList = instanceOfRepository!!.messageDAO().getAllMessages()
        return messageList
    }

    fun getLastMessage(context: Context): LiveData<MutableList<MessageEntity>>?
    {
        instanceOfRepository = initializeDB(context)
        messageList = instanceOfRepository!!.messageDAO().getAllMessages()
        return messageList
    }


}