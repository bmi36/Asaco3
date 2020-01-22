package com.example.asaco2.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.sql.SQLException

class CalendarViewModel(application: Application) : AndroidViewModel(application) {

    private val calendarRepository: CalendarRepository
    private var repoDao: CalendarDao = CalendarDatabase.getInstance(application).calendarDao()

    init {
        calendarRepository = CalendarRepository(repoDao)
    }

    fun insert(entity: CalendarEntity) = viewModelScope.launch { calendarRepository.insert(entity) }

    fun update(entity: CalendarEntity) = viewModelScope.launch { calendarRepository.update(entity) }

    fun InsertOrUpdata(entity: CalendarEntity) = viewModelScope.launch {
        try {
            calendarRepository.insert(entity)
        } catch (e: SQLException) {
            e.printStackTrace()
            calendarRepository.update(entity)
        }
    }

    fun getCalendar(id: Long): List<CalendarEntity>? = calendarRepository.getCalendar(id)
}

class StepViewModel(application: Application) : AndroidViewModel(application) {
    private val stepRepository: StepRepository

    private var repoDao: StepDao = StepDataBase.getInstance(application).dao()

    init {
        stepRepository = StepRepository(repoDao)
    }

    fun getstep(date: Long): Array<Step> = viewModelScope.run { repoDao.getEntity(date) }
    fun getsumstep(date: Long): Int = viewModelScope.run { repoDao.getstep(date) }
    fun insert(step: Step) = viewModelScope.launch { repoDao.insert(step) }
    fun update(step: Step) = viewModelScope.launch { repoDao.update(step) }
    fun InsertOrUpdata(entity: Step) = viewModelScope.launch {
        try {
            stepRepository.insert(entity)
        } catch (e: SQLException) {
            e.printStackTrace()
            stepRepository.update(entity)
        }
    }
}