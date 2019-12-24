package com.example.asaco2.ui.home

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
class Repository(private val dao: CalendarDao) {
    val allEntity: LiveData<Array<CalendarEntity>> = dao.getEntity()

    @WorkerThread
    suspend fun insert(entity: CalendarEntity){
        dao.insert(entity)
    }

    suspend fun update(entity: CalendarEntity){
        dao.update(entity)
    }
    fun getCalendar(id: Long): CalendarEntity? {
        return dao.getEntity(id)
    }

}

