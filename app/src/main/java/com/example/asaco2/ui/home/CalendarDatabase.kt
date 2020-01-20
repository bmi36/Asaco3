package com.example.asaco2.ui.home

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [CalendarEntity::class], version = 1)
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

@Database(entities = [Step::class],version = 1)
abstract class StepDataBase: RoomDatabase(){
    abstract fun dao(): StepDao

    companion object{
        @Volatile
        private var instance: StepDataBase? = null
        fun getInstance(context: Context): StepDataBase = instance ?:
                synchronized(this){
                    Room.databaseBuilder(context,StepDataBase::class.java,"step_db").build()
                }
    }
}
