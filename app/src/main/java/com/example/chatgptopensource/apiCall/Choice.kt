package com.example.chatgptopensource.apiCall

data class Choice (
    val index: Int,
    val message: Message,
    val finish_reason: String,
    )
