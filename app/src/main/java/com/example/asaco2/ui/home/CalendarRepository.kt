package com.example.asaco2.ui.home

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import java.util.*

class CalendarRepository(private val dao: CalendarDao) {
    val allEntity: LiveData<Array<CalendarEntity>> = dao.getEntity()

    @WorkerThread
    suspend fun insert(entity: CalendarEntity){ dao.insert(entity) }

    suspend fun update(entity: CalendarEntity){ dao.update(entity) }

    fun getCalendar(id: Long): List<CalendarEntity>? { return dao.getEntity(id) }

}

