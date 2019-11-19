package com.example.asacojoin

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.myapplication.CalendarDao
import com.example.myapplication.CalendarEntity

class Repository(private val dao: CalendarDao) {
    val allEntity: LiveData<Array<CalendarEntity>> = dao.getEntity()

    @WorkerThread
    suspend fun insert(entity: CalendarEntity){
        dao.insert(entity)
    }

    suspend fun update(entity: CalendarEntity){
        dao.update(entity)
    }

}