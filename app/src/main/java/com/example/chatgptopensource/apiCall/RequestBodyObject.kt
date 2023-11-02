package com.example.chatgptopensource.apiCall

data class RequestBodyObject(
    val model: String,
    val messages: List<Message>
)