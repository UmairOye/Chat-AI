package com.example.chatgptopensource.apiCall

import com.example.chatgptopensource.utils.Constants
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers(
        "Content-Type: application/json",
        "Authorization: Bearer ${Constants.API_KEY}"
    )
    @POST("chat/completions")
    suspend fun getChatCompletion(@Body requestBody: RequestBodyObject): ChatCompletionResponse


}