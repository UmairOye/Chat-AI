package com.example.chatgptopensource.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MessageDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessage(model: MessageEntity)

    @Query("SELECT * FROM chatTbl ORDER BY id DESC")
    fun getAllMessages(): LiveData<MutableList<MessageEntity>>?

    @Query("SELECT * FROM chatTbl ORDER BY id DESC LIMIT 2")
    fun getLastMessage(): LiveData<MutableList<MessageEntity>>?
}