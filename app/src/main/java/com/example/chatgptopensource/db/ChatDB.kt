package com.example.chatgptopensource.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MessageEntity::class] , version = 1, exportSchema = false)
abstract class ChatDB : RoomDatabase() {
    abstract fun messageDAO() : MessageDAO

    companion object {

        @Volatile
        private var INSTANCE: ChatDB? = null
        fun getDatabaseClient(context: Context) : ChatDB {
            if (INSTANCE != null) return INSTANCE!!
            synchronized(this) {
                INSTANCE = Room
                    .databaseBuilder(context, ChatDB::class.java, "chatAIdB")
                    .fallbackToDestructiveMigration()
                    .build()

                return INSTANCE!!

            }
        }

    }


}