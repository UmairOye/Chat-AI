package com.example.chatgptopensource.data.remote

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.chatgptopensource.R
import com.example.chatgptopensource.apiCall.ChatCompletionResponse
import com.example.chatgptopensource.apiCall.Choice
import com.example.chatgptopensource.apiCall.Message
import com.example.chatgptopensource.apiCall.RequestBodyObject
import com.example.chatgptopensource.apiCall.Usage
import com.example.chatgptopensource.db.MessageEntity
import kotlinx.coroutines.Dispatchers

class ChatViewModel : ViewModel() {
    private val repository = ChatRepository()
    var messageFromUser: MutableLiveData<Message> = MutableLiveData()
    var isEdiTextShown: MutableLiveData<Boolean> = MutableLiveData()
    var isResponseArrived: MutableLiveData<Boolean> = MutableLiveData()

    val listOfChoices: ArrayList<Choice> = ArrayList()
    val usage = Usage(1, 1, 1)

    fun getChatCompletion(requestBody: RequestBodyObject, context: Context) = liveData(Dispatchers.IO) {
        try {
            val response = repository.getChatCompletion(requestBody)
            emit(response)
        } catch (e: Exception) {
            val message = Message("assistant",
                context.getString(
                    R.string.timeout,
                    e.message.toString()
                ))
            listOfChoices.add(Choice(0, message, ""))
            emit(ChatCompletionResponse("", "", 0L, "", listOfChoices, usage))
        }
    }


    fun insertMessages(context: Context, model: MessageEntity) {
        repository.insertData(context, model)
    }

    fun getAllMessages(context: Context): LiveData<MutableList<MessageEntity>>? {
        return repository.getAllMessages(context)
    }

    fun getLastMessage(context: Context): LiveData<MutableList<MessageEntity>>?
    {
        return repository.getLastMessage(context)
    }

}