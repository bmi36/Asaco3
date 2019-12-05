package com.example.asaco2.ui.home

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CalendarEntity::class], version = 1, exportSchema = false)
 abstract class CalendarDatabase : RoomDatabase(){

    abstract fun calendarDao(): CalendarDao

    companion object{
        @Volatile
        private var instance: CalendarDatabase? = null

        fun getInstance(context: Context): CalendarDatabase = instance ?:
                synchronized(this){
                    Room.databaseBuilder(context, CalendarDatabase::class.java,"calendar_db").build()
                }
    }
}
