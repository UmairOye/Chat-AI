package com.example.chatgptopensource.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chatTbl")
data class MessageEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val role: String,
    val content: String
)